# SanSi
The continuation of number series is a prototypical intelligence test task. Archetypal examples for number series to continue are

```
1, 3, 5, 7, ...
2, 4, 8, 16, 32, ...
2, 3, 5, 7, 13, 17, ...
1, 1, 2, 3, 5, 8, 13, ...
```
The SanSi-system operates on algorithmic, function-based definition for number series, like `x(0) = 1, x(n) = x(n-1)+2` for the first series given above.
The system is able to randomly generate such definitions, generate a number series of arbitrary length from such a definition, and induce (or fit) a definitions for a given number series.
The induction is a combination of cognition-inspired enumeration and analytic fitting. Thus, the name Semi-analytic number Series induction (SanSi).
For more information on the induction process see the paper:

> Siebers M., Schmid U. (2012) Semi-analytic Natural Number Series Induction. In: Glimm B., Krüger A. (eds) KI 2012: Advances in Artificial Intelligence, p. 249–252. Springer, Berlin, Heidelberg. https://doi.org/10.1007/978-3-642-33347-7_25

## Applications
The SanSi-system consists of several applications which are all included in this repository. The applications are available as runnable jar-files in the `dist` subdirectory.

Executable | Main Class | Description
-----------|------------|------------
series.jar | app.ConsoleApp | Induces a number series definition for a number series given on the command line and predicts the next number.
creation.jar | app.CreationApp | Creates number series definitions randomly and produces number series of given length from the definitions.
batch.jar | apps.BatchApp | Reads a list of number series from file. For each series, it induces a definition from the initial numbers and tests the correctness of the definition on the last three numbers.
gui.jar | app.GUIApp | A GUI version of app.ConsoleApp: induces a number series definition for a given number series and predicts the following numbers.
collect_gui.jar | app.CollectionGUIApp | Induces a list of number series definitions for a given number series and predicts the following numbers from the selected definition.

### ConsoleApp
The app requires the number series to induce a definition for as only argument.
Numbers in the series are separated by commatas (,). The series may not contain whitespace.
The outputs the definition found for the series and the next number. In the definition the following conventions are used:
- `x` the function producing the series
- `n` the position of the current number (starting with 0).
- `y` an auxiliary number series
- `+`, `-`, `*`, `/`, and `^` mathematical operations addition, subtraction, multiplication, division, and exponentiation, as usual.

### BatchApp
This app requires two command line arguments: an input file and an output file.
The input contains number series whose definitions shall be induced and will be written into the output file.
Each number series is split into an induction part which is used for inducing the definition and a test part
which is used to test the correcteness of the induced definition. If possible, the test part has a length of 3.

#### Input Format
The input file must contain one number series per line. A number series starts with an arbitrary name 
(verbally copied to the output file) and a list of numbers. Name and numbers must be separated by a 
semi-colon (;), individual numbers must be separated by commata (,). Whitespace around commatas is ignored.

#### Output Format
The output file contains a table with 6 columns, separated by tabs: 
 - name: contains the name of the number series as given in the input file
 - split point: the index of the number series where it was divided into induction and test part
 - correct: whether the induced number series's prediction matched the test part (correct) or not (wrong)
 - definition: the induced definition (or -- if none was found)
 - depth: the depth of the expression used for the definition
 - time: the time in ms required to induce the number series definition

### CreationApp
This app requires two command line arguents: the number of series to create and the number of numbers to create per series.
The app generates two files: a list of number series definitions `./resources/definitions.csv` and a list of number series `./resources/series.csv`. The filenames _cannot_ be changed from the command line.

#### Series File Format
The format of the series file follows the [input format of BatchApp](#input-format). Series named sequentially as "Sxxx" where `xxx`is the number of the series with leading zeros as required.

#### Definition File Format
The definition file contains one definition per line. The definition starts with the name of the series (as in the series file) followed by a semi-colon (;). The the series definition is printed as in the output of [ConsoleApp](#consoleapp).
