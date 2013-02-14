./clean && echo "Cleaned." && ./process && echo "Processed." && ./compile && echo "Compiled." && echo "~~~~~~" && ./run $1 && echo "~~~~~~" && echo "Done!"
