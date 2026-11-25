"""CSC1002 Assignment 3: Snake."""

import random
import time
import turtle
from functools import partial


# Board coordinates are relative to the center of the motion area.
CELL_SIZE = 20
BOARD_SIZE = 500
STATUS_HEIGHT = 60
MARGIN = 30
BOARD_OFFSET = -STATUS_HEIGHT // 2
EDGE = (BOARD_SIZE - CELL_SIZE) // 2
GRID = range(-EDGE, EDGE + 1, CELL_SIZE)
DIRECTIONS = {
    "Up": (0, CELL_SIZE),
    "Down": (0, -CELL_SIZE),
    "Left": (-CELL_SIZE, 0),
    "Right": (CELL_SIZE, 0),
}
START_HEAD = (0, 0)
INITIAL_TAIL = 5
FOOD_VALUES = range(1, 6)
FOOD_SHIFT = 2
MONSTER_COUNT = 4
MONSTER_SPAWN_DISTANCE = 300
SNAKE_INTERVAL = 250
GROWTH_INTERVAL = 300
# Monsters can gain ground while the snake is growing.
MONSTER_INTERVAL = (280, 360)
FOOD_INTERVAL = (5000, 10000)
CLOCK_INTERVAL = 250

INK = "#182030"
HEAD_COLOR = "#cf223e"
BODY_COLOR = "#101622"
BODY_OUTLINE = "#5671d7"
MONSTER_COLOR = "#b986e8"


def main():
    """Show the opening screen and wait for the player's first click."""
    game = new_game()
    view = create_view()
    screen = view["screen"]
    screen.onscreenclick(partial(start_game, game, view))
    refresh_game(game, view)
    screen.mainloop()


def new_game():
    """Create the initial game state before the clock and movement start."""
    return {
        "snake": [START_HEAD],
        "length_limit": INITIAL_TAIL + 1,
        "growth_left": 0,
        "monsters": spawn_monsters(),
        "food": {},
        "direction": None,
        "paused": True,
        "contacts": 0,
        "started_at": None,
        "ended_at": None,
        "result": "",
    }


def start_game(game, view, _x, _y):
    """Reveal the food, enable the controls and start the game timers."""
    if game["started_at"] is not None:
        return

    screen = view["screen"]
    screen.onscreenclick(None)
    game["food"] = spawn_food(game)
    game["started_at"] = time.monotonic()
    bind_controls(game, view)
    refresh_game(game, view)

    screen.ontimer(partial(snake_tick, game, view), SNAKE_INTERVAL)
    for index in range(MONSTER_COUNT):
        schedule_monster(game, view, index)
    screen.ontimer(partial(food_tick, game, view), random.randint(*FOOD_INTERVAL))
    screen.ontimer(partial(clock_tick, game, view), CLOCK_INTERVAL)


def bind_controls(game, view):
    """Listen for arrow keys and the spacebar while the game is running."""
    screen = view["screen"]
    for direction in DIRECTIONS:
        screen.onkeypress(partial(steer, game, view, direction), direction)
    screen.onkey(partial(toggle_pause, game, view), "space")
    screen.listen()


def steer(game, view, direction):
    """Set the new direction and resume movement, including on reversals."""
    if game["result"] or (direction == game["direction"] and not game["paused"]):
        return
    game["direction"] = direction
    game["paused"] = False
    refresh_status(game, view)


def toggle_pause(game, view):
    """Pause or resume the snake after the player has chosen a direction."""
    if game["result"] or game["direction"] is None:
        return
    game["paused"] = not game["paused"]
    refresh_status(game, view)


def snake_tick(game, view):
    """Run one snake timer event and choose the interval for the next one."""
    if game["result"]:
        return

    if not game["paused"] and move_snake(game):
        check_result(game)
        refresh_game(game, view)

    if not game["result"]:
        interval = GROWTH_INTERVAL if game["growth_left"] else SNAKE_INTERVAL
        view["screen"].ontimer(partial(snake_tick, game, view), interval)


def schedule_monster(game, view, index):
    """Give each monster its own randomly timed chase steps."""
    interval = random.randint(*MONSTER_INTERVAL)
    view["screen"].ontimer(partial(monster_tick, game, view, index), interval)


def monster_tick(game, view, index):
    """Move one monster, count its contact and check for a head collision."""
    if game["result"]:
        return

    move_monster(game, index)
    check_result(game)
    refresh_game(game, view)
    if not game["result"]:
        schedule_monster(game, view, index)


def food_tick(game, view):
    """Move an unconsumed food item at a random interval."""
    if game["result"] or not game["food"]:
        return

    if shift_food(game):
        refresh_game(game, view)
    if not game["result"] and game["food"]:
        interval = random.randint(*FOOD_INTERVAL)
        view["screen"].ontimer(partial(food_tick, game, view), interval)


def clock_tick(game, view):
    """Refresh elapsed time even when the snake is paused or blocked."""
    if game["result"]:
        return
    refresh_status(game, view)
    if not game["result"]:
        view["screen"].ontimer(partial(clock_tick, game, view), CLOCK_INTERVAL)


def move_snake(game):
    """Advance one cell, extending the tail one square per pending unit."""
    snake = game["snake"]
    head = offset_cell(snake[0], DIRECTIONS[game["direction"]])
    if not inside_board(head):
        return False

    # Apply growth already earned before recording food eaten on this move.
    if game["growth_left"]:
        game["length_limit"] += 1
        game["growth_left"] -= 1
    snake.insert(0, head)
    if len(snake) > game["length_limit"]:
        snake.pop()

    game["growth_left"] += game["food"].pop(head, 0)
    return True


def move_monster(game, index):
    """Take a cardinal step toward the head and count at most one contact."""
    current = game["monsters"][index]
    head = game["snake"][0]
    candidates = open_steps(current)
    random.shuffle(candidates)
    position = min(candidates, key=partial(grid_distance, head))
    game["monsters"][index] = position
    if any(overlaps(position, segment) for segment in game["snake"]):
        game["contacts"] += 1


def check_result(game):
    """End on a head collision or when all food and tail growth are complete."""
    snake = game["snake"]
    if any(overlaps(snake[0], monster) for monster in game["monsters"]):
        game["result"] = "Game Over !!"
    elif (not game["food"] and not game["growth_left"]
          and len(snake) == game["length_limit"]):
        game["result"] = "Winner !!"
    if game["result"]:
        game["ended_at"] = time.monotonic()


def spawn_monsters():
    """Choose separated starting positions a fair distance from the head."""
    coordinates = range(-EDGE + CELL_SIZE, EDGE, 2 * CELL_SIZE)
    candidates = [
        (x, y) for x in coordinates for y in coordinates
        if grid_distance(START_HEAD, (x, y)) >= MONSTER_SPAWN_DISTANCE
    ]
    # Offset the monsters from the snake's grid so a collision can visibly overlap.
    offsets = (-5, -4, -3, 3, 4, 5)
    return [
        (x + random.choice(offsets), y + random.choice(offsets))
        for x, y in random.sample(candidates, MONSTER_COUNT)
    ]


def spawn_food(game):
    """Place one copy of each food value on separate, empty snake-grid cells."""
    candidates = [(x, y) for x in GRID for y in GRID if free_for_food(game, (x, y))]
    locations = random.sample(candidates, len(FOOD_VALUES))
    return dict(zip(locations, FOOD_VALUES))


def shift_food(game):
    """Move one randomly chosen movable number without losing or hiding it."""
    available_moves = {}
    for position in game["food"]:
        destinations = [
            cell for cell in open_steps(position, FOOD_SHIFT)
            if free_for_food(game, cell)
        ]
        if destinations:
            available_moves[position] = destinations

    if not available_moves:
        return False

    source = random.choice(list(available_moves))
    destination = random.choice(available_moves[source])
    game["food"][destination] = game["food"].pop(source)
    return True


def free_for_food(game, position):
    """Check that a number will be visible and reachable at this position."""
    return (
        position not in game["food"]
        and position not in game["snake"]
        and not any(overlaps(position, monster) for monster in game["monsters"])
    )


def open_steps(position, cells=1):
    """Return the in-bounds destinations for an orthogonal move."""
    destinations = [offset_cell(position, step, cells) for step in DIRECTIONS.values()]
    return [destination for destination in destinations if inside_board(destination)]


def offset_cell(position, step, cells=1):
    return (position[0] + cells * step[0], position[1] + cells * step[1])


def inside_board(position):
    x, y = position
    return -EDGE <= x <= EDGE and -EDGE <= y <= EDGE


def grid_distance(first, second):
    """Measure distance in pixels along horizontal and vertical moves."""
    return abs(first[0] - second[0]) + abs(first[1] - second[1])


def overlaps(first, second):
    """Check whether the interiors of two default-sized squares intersect."""
    dx = abs(first[0] - second[0])
    dy = abs(first[1] - second[1])
    return dx < CELL_SIZE and dy < CELL_SIZE


def create_view():
    """Create the two bordered areas and the turtles used for rendering."""
    screen = turtle.Screen()
    screen.setup(BOARD_SIZE + 2 * MARGIN, BOARD_SIZE + STATUS_HEIGHT + 2 * MARGIN)
    screen.title("Snake")
    screen.bgcolor("white")
    screen.tracer(0)

    border = make_pen()
    half = BOARD_SIZE // 2
    bottom = BOARD_OFFSET - half
    draw_border(border, -half, bottom, BOARD_SIZE, BOARD_SIZE)
    draw_border(border, -half, bottom + BOARD_SIZE, BOARD_SIZE, STATUS_HEIGHT)

    return {
        "screen": screen,
        "tail": make_pen(BODY_OUTLINE, BODY_COLOR),
        "head": make_pen(HEAD_COLOR),
        "monsters": [make_pen(MONSTER_COLOR) for _ in range(MONSTER_COUNT)],
        "food": {value: make_pen() for value in FOOD_VALUES},
        "status": make_pen(),
        "message": make_pen(),
    }


def make_pen(color=INK, fill=None):
    pen = turtle.Turtle(shape="square", visible=False)
    pen.penup()
    pen.color(color, color if fill is None else fill)
    return pen


def draw_border(pen, x, y, width, height):
    pen.goto(x, y)
    pen.pendown()
    for corner in ((x + width, y), (x + width, y + height), (x, y + height), (x, y)):
        pen.goto(corner)
    pen.penup()


def refresh_game(game, view):
    """Draw a frame in layers so food and end messages remain readable."""
    draw_snake(game["snake"], view)
    for pen, position in zip(view["monsters"], game["monsters"]):
        pen.clearstamps()
        stamp_cell(pen, position)
    draw_food(game["food"], view["food"])
    draw_message(game, view["message"])
    refresh_status(game, view)


def draw_snake(snake, view):
    tail = view["tail"]
    tail.clearstamps()
    for segment in reversed(snake[1:]):
        stamp_cell(tail, segment)
    head = view["head"]
    head.clearstamps()
    stamp_cell(head, snake[0])


def stamp_cell(pen, position):
    x, y = position
    pen.goto(x, y + BOARD_OFFSET)
    pen.stamp()


def draw_food(food, pens):
    for pen in pens.values():
        pen.clear()
    for (x, y), value in food.items():
        pens[value].goto(x + 1, y + BOARD_OFFSET - 11)
        pens[value].write(value, align="center", font=("Arial", -18, "bold"))


def draw_message(game, pen):
    pen.clear()
    if game["started_at"] is None:
        pen.goto(0, BOARD_OFFSET + 126)
        pen.write("Click anywhere to start, have fun!!!!", align="center",
                  font=("Arial", -19, "bold"))
        pen.goto(0, BOARD_OFFSET + 97)
        pen.write("Eat all five numbers and keep your head away from the monsters.",
                  align="center", font=("Arial", -13, "normal"))
        pen.goto(0, BOARD_OFFSET + 76)
        pen.write("Arrow keys to steer. Space to pause or resume the snake.",
                  align="center", font=("Arial", -13, "normal"))
    elif game["result"]:
        pen.color(HEAD_COLOR)
        pen.goto(0, BOARD_OFFSET - 15)
        pen.write(game["result"], align="center", font=("Arial", -26, "bold"))


def elapsed_seconds(game):
    if game["started_at"] is None:
        return 0
    end = game["ended_at"] if game["result"] else time.monotonic()
    return int(end - game["started_at"])


def refresh_status(game, view):
    pen = view["status"]
    pen.clear()
    motion = "Paused" if game["paused"] else game["direction"]
    labels = (f"Contact: {game['contacts']}", f"Time: {elapsed_seconds(game)} s",
              f"Motion: {motion}")
    for x, text in zip((-166, 0, 166), labels):
        pen.goto(x, BOARD_SIZE // 2 - 11)
        pen.write(text, align="center", font=("Arial", -17, "bold"))
    view["screen"].update()


if __name__ == "__main__":
    main()
