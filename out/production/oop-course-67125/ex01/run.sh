#!/bin/bash

# Define the command to run
command="Tournament"

# Check if the required number of arguments are provided
if [ "$#" -ne 6 ]; then
    echo "Usage: $0 <num_games> <size_of_board> <win_streak> <renderer> <player1_type> <player2_type>"
    exit 1
fi

# Get the arguments from the script
num_games=$1
size_of_board=$2
win_streak=$3
renderer=$4
player1_type=$5
player2_type=$6

# Create a file to store the output
output_file="output.txt"

# Run the command 100 times and append the output to the file
for i in {1..100}; do
    java $command $num_games $size_of_board $win_streak $renderer $player1_type $player2_type >> $output_file
done

# Print the contents of the output file
echo "Output:"
cat $output_file