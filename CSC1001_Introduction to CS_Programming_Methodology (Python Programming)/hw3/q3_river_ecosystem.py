import random


class Ecosystem:
    def __init__(self, river_length, num_fish, num_bears):
        # Initialize the river
        self.river = [None] * river_length

        # Populate the river with fish and bears
        # Fish are represented by 'F', bears are represented by 'B', 'N' represents an empty space
        # Todo: randomly place the fish and bears in the river
        if min(river_length, num_fish, num_bears) < 0 or num_fish + num_bears > river_length:
            raise ValueError("Animal counts must be nonnegative and fit in the river.")

        # String subclasses keep the supplied display code compatible with animal objects.
        class Fish(str):
            pass

        class Bear(str):
            pass

        animals = [Fish('F') for _ in range(num_fish)]
        animals += [Bear('B') for _ in range(num_bears)]
        positions = random.sample(range(river_length), len(animals))
        for position, animal in zip(positions, animals):
            self.river[position] = animal

        # The assignment text calls this simulation, while the template uses simulate.
        self.simulation = self.simulation

        # display the initial river
        print(self.display_river())

    def simulation(self, steps):
        for _ in range(steps):
            # Create a copy of the river to update animals' positions
            new_river = self.river.copy()

            # Todo: move the animals in the river
            # Only surviving animals present at the start of the step take turns.
            for position, animal in enumerate(self.river):
                if animal is None or new_river[position] is not animal:
                    continue

                direction = random.choice((-1, 0, 1))
                if direction == 0:
                    continue

                destination = position + direction
                if not 0 <= destination < len(new_river):
                    new_river[position] = None
                    continue

                other_animal = new_river[destination]
                if other_animal == animal:
                    empty_positions = [
                        index for index, cell in enumerate(new_river) if cell is None
                    ]
                    if empty_positions:
                        new_river[random.choice(empty_positions)] = type(animal)(animal)
                else:
                    if other_animal is None or animal == 'B':
                        new_river[destination] = animal
                    new_river[position] = None

            # Update the river state
            self.river = new_river

            # desplay the river
            print(self.display_river())

    def display_river(self):
        return ''.join(['N' if x is None else x for x in self.river])


if __name__ == '__main__':
    # Test 1
    ecosystem = Ecosystem(river_length=3, num_fish=1, num_bears=1)
    ecosystem.simulation(steps=2)

    # Test 2
    ecosystem = Ecosystem(river_length=10, num_fish=3, num_bears=2)
    ecosystem.simulation(steps=5)

    # Test 3
    ecosystem = Ecosystem(river_length=8, num_fish=2, num_bears=1)
    ecosystem.simulation(steps=10)

    # Test 4
    ecosystem = Ecosystem(river_length=10, num_fish=2, num_bears=3)
    ecosystem.simulation(steps=10)
