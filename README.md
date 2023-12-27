# Data parsing
This program is written entirely in Java Core. Its task is to parse the input data in the report-file and process all operations (successful or unsuccessful ) . The program must have an input file called 
"file-with-numbers" on your desktop, otherwise the program will not work. The remaining elements - input folder,acrhive folder and report-file will be created if they do not exist.
## Details
1.The program processes only those values of file-with-numbers that satisfy the format XXXXX-XXXXX: Y ; where X are digits of the same value (For example 11111-11111)
It means - the format is provided only for values such as:
11111-11111:
22222-22222:
33333-33333:
Y - integers only (e.g. 100 , 200  and etc. (except)
2.The program provides new parsing when adding new files to input when the program is still running.
3.The program filters input files in txt format only.
4.The report-file is also in txt format.
5.The input files that have been checked get to the archive folder only after the program is completed (3. Exit)
### Technologies used
Java SE 17
