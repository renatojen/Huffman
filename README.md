# huffman
Compress/inflate files using Huffman algorythm
Language: Java

Usage:

To compress a file (i.e. the 'dna.txt' file in resources):
After compiling the project, run the following command (from the project dir):
java -classpath .\bin br.com.jensen.main.Huff -c ".\resources\dna.txt" -o ".\resources\dna.huff"

To inflate a (previously) compressed file (i.e. the 'dna.txt' file in resources):
After compiling the project, run the following command (from the project dir):
java -classpath .\bin br.com.jensen.main.Huff -x ".\resources\dna.huff" -o ".\resources\dna_x.txt"
