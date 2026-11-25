import turtle
import random

#global variables
board_size = 3  # The size of the puzzle (default is 3x3)
tile_size = 200  # Size of each tile in the puzzle
empty_tile = (0, 0)  # The position of the empty tile
tiles = []  # A nested list storing the tiles
screen = turtle.Screen()#Create a turtle screen
t = turtle.Turtle()#Create a turtle object
t.speed(0)
t.hideturtle()
color = 'Blue'
game_completed = False#Indicator of the game's status

def init_game(dimensions):
    #Initialize the game by setting up the board and shuffling the tiles
    global board_size, tiles, empty_tile

    board_size = dimensions
    screen.setup(width=board_size * tile_size, height=board_size * tile_size)
    turtle.title("Slide Puzzle Game")

    numbers = list(range(1, board_size * board_size))
    numbers.append(0)

    # Shuffle the numbers and check if the layout is solvable
    while True:
        random.shuffle(numbers)
        if is_solvable(numbers, board_size):
            break

    #Create the nested list to store the puzzle
    tiles = [numbers[i:i+board_size] for i in range(0, len(numbers), board_size)]
    
    # Find the position of the empty tile
    for i in range(board_size):
        for j in range(board_size):
            if tiles[i][j] == 0:
                empty_tile = (i, j)

    display_puzzle()



def is_solvable(numbers, dimensions):
    #Use inversion to check whether the puzzle is solvable
    inversions = 0
    blank_row = 0  # The row number from bottom where the blank tile is
    for i in range(len(numbers)):
        if numbers[i] == 0:
            blank_row = dimensions - (i // dimensions)
            continue
        for j in range(i + 1, len(numbers)):
            if numbers[i] > numbers[j] and numbers[j] != 0:
                inversions += 1

    if dimensions % 2 == 0:  # If the board size is even
        if blank_row % 2 == 0:  # Blank on even row from bottom
            return inversions % 2 != 0
        else:  # Blank on odd row from bottom
            return inversions % 2 == 0
    else:  # If the board size is odd
        return inversions % 2 == 0



def display_puzzle():
    #Display the puzzle on the screen
    turtle.tracer(0, 0)
    t.clear()

    global color, game_completed
    color = 'red' if game_completed else 'Green'#Change color when the game is finished

    #Layout the tiles
    for i in range(board_size):
        for j in range(board_size):
            draw_tile(i, j, tiles[i][j])

    turtle.update()

def draw_tile(row, col, number):
    #Draw individual tile on the screen
    global color
    color = 'red' if game_completed else 'Green'

    draw_size = tile_size - 8
    x = col * tile_size - board_size * tile_size / 2 + 4
    y = board_size * tile_size / 2 - row * tile_size - 4

    #Use turtle to finish the drawing

    t.penup()
    t.goto(x, y)
    t.pendown()

    if number > 0:
        t.fillcolor(color)
        t.pencolor(color)
        t.begin_fill()
        for _ in range(4):
            t.forward(draw_size)
            t.right(90)
        t.end_fill()

        t.penup()
        t.goto(x + draw_size / 2, y - draw_size / 2 - 20)
        t.pencolor("blue")
        t.write(number, align="center", font=("Arial", 18, "normal"))
        t.pencolor("black")

def move_tile(row, col):
    #Move the tile adjacent to the empty space to the empty space
    global empty_tile, game_completed

    if game_completed:
        return

    #Check if the tile is adjacent to the empty block
    #If adjacent, swap their positions
    if (abs(empty_tile[0] - row) == 1 and empty_tile[1] == col) or (abs(empty_tile[1] - col) == 1 and empty_tile[0] == row):
        tiles[empty_tile[0]][empty_tile[1]], tiles[row][col] = tiles[row][col], tiles[empty_tile[0]][empty_tile[1]]
        empty_tile = (row, col)
        display_puzzle()
        #Check if the game is solved
        if check_solved():
            game_completed = True
            display_puzzle()#Redraw the puzzle to show the completed state

def check_solved():
    #Check whether the puzzle is solved
    global tiles
    solved = [[i * board_size + j + 1 for j in range(board_size)] for i in range(board_size)]
    solved[board_size - 1][board_size - 1] = 0
    return tiles == solved

def tile_click(x, y):
    #Trace the mouse click
    col = int((x + board_size * tile_size / 2) // tile_size)
    row = int((board_size * tile_size / 2 - y) // tile_size)
    move_tile(row, col)


def main():
    dimensions = int(turtle.numinput("Slide Puzzle", "Enter the dimensions of the puzzle (e.g., 3 for 3x3):", default=3))#Get the puzzle's dimension
    init_game(dimensions)
    screen.onclick(tile_click)
    turtle.mainloop()

if __name__ == "__main__":
    main()
