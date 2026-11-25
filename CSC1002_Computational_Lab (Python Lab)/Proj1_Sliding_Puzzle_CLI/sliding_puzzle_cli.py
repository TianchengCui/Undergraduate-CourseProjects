import random
import re

def display_intro():
    #Display the rules of the game
    print("Welcome to my puzzle game！")
    print("You will be prompt to solve the sliding puzzle.")
    print("You can choose four letter on your keyboard to make the left, right, up and down move.")
    print("e.g. If you choose 'l' to make a LEFT move, the number on the RIGHT of the blank")
    print("will move to fill the blank.")
    print("When you get an output from 1-8 and the blank at the bottom right corner, you WIN!")

def get_move():
    #Return the valid moves letters entered by the user or ask them to input again
    while True:
        valid_moves = input("Enter the four letters used for left, right, up and down move, RESPECTIVELY!!!（e.g. l、r、u、d）：")
        m = valid_moves.lower()#Make the input regardless of case
        m = re.sub(r'\s+', '', m)#Make the input regardless of blank spaces
        if re.match(r'^[a-z]{4}$', m) and len(set(m)) == 4:#Check if the input matches 4 different lowercase letters
            return m 
        else:
            print("Invalid input! Please enter FOUR DIFFERENT LETTERS again")

def generate_puzzle():
    #Generate a randomized puzzle
    puzzle = [1, 2, 3, 4, 5, 6, 7, 8, ' ']
    random.shuffle(puzzle)
    return puzzle

def print_puzzle(puzzle):
    #Display the puzzle 
    print()
    print(f" {puzzle[0]}   {puzzle[1]}   {puzzle[2]}")
    print(f" {puzzle[3]}   {puzzle[4]}   {puzzle[5]}")
    print(f" {puzzle[6]}   {puzzle[7]}   {puzzle[8]}")
    print()

def make_move(puzzle, direction, moves):
    puzzle_copy = puzzle.copy()#Create a copy to avoid modifying the initial puzzle
    idx_empty = puzzle_copy.index(' ')#Locate the index of the blank space
    idx_move = None#Initialize the variable  to store the index

    dir_dict = {moves[i]: i for i in range(4)}  # Create a dictionary to map user's moves

    if direction == moves[0] and idx_empty % 3 != 2:  # Moving the number to the right
        idx_move = idx_empty + 1
    elif direction == moves[1] and idx_empty % 3 != 0:  # Moving the number to the left
        idx_move = idx_empty - 1
    elif direction == moves[2] and idx_empty < 6:  # Moving the number down
        idx_move = idx_empty + 3
    elif direction == moves[3] and idx_empty >= 3:  # Moving the number up
        idx_move = idx_empty - 3

    if idx_move is not None:
        puzzle_copy[idx_move], puzzle_copy[idx_empty] = puzzle_copy[idx_empty], puzzle_copy[idx_move]#Swap the empty space and the number

    return puzzle_copy



def is_solved(puzzle):
    return puzzle == [1, 2, 3, 4, 5, 6, 7, 8, ' ']

def count_inversions(puzzle):
    #Method to verify whether the puzzle is solvable
    inversions = 0
    for i in range(len(puzzle)):
        if puzzle[i] == ' ':
            continue
        for j in range(i + 1, len(puzzle)):
            if puzzle[j] != ' ' and puzzle[i] > puzzle[j]:
                inversions += 1
    return inversions

def is_solvable(puzzle):
    #Check if the random puzzle is solvable
    inversions = count_inversions(puzzle)
    if inversions % 2 == 0:
        return True
    else:
        return False

def play_game():
    display_intro()
    moves = get_move()
    puzzle = generate_puzzle()
    while not is_solvable(puzzle):#Generate the puzzle until it is solvable
        puzzle = generate_puzzle()
    print_puzzle(puzzle)
    move_count = 0

    while not is_solved(puzzle):#While it is not solved, prompt the user to make moves
        valid_moves = []
        idx_empty = puzzle.index(' ')

        if idx_empty % 3 != 2:  # Can move number to the right
            valid_moves.append(moves[0])
        if idx_empty % 3 != 0:  # Can move number to the left
            valid_moves.append(moves[1])
        if idx_empty < 6:  # Can move number down
            valid_moves.append(moves[2])
        if idx_empty >= 3:  # Can move number up
            valid_moves.append(moves[3])

        move = input(f"Enter your move（valid moves：{', '.join(valid_moves)}）：")
        move = move.lower()

        if move in valid_moves:
            puzzle = make_move(puzzle, move, moves)  #Make the move and update the puzzle
        else:
            print("Invalid move! Please try again.")
            continue

        move_count += 1
        print_puzzle(puzzle)

    print(f"Congratulations！You solve the puzzle in {move_count} moves!")

def main():
    #Start the game and check whether the user wants to play again 
    while True:
        play_game()
        choice = input("Enter 'n' for another game，or 'q' to end the game：")
        if choice.lower() == 'q':
            break

if __name__ == "__main__":
    main()
