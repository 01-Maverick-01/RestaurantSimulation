# CSE 6431 Operating System - Lab

### Environment:
* **OS:** Ubuntu 18.04.3 LTS
* **Java:**
    * openjdk version "11.0.6" 2020-01-14
    * OpenJDK Runtime Environment (build 11.0.6+10-post-Ubuntu-1ubuntu118.04.1)
    * OpenJDK 64-Bit Server VM (build 11.0.6+10-post-Ubuntu-1ubuntu118.04.1, mixed mode, sharing)
**GNU Make (4.1)**

### Build Instruction
1. Launch terminal in the '<SubmissionFolder>/src' folder.
1. Execute cmd: make

### Execution
1. Launch terminal in the src folder.
1. Execute on of the below commands:
    1. `./Restaurant` <br/>
        >if you use this cmdLine to invoke program, then default input file(input.txt stored in testfiles folder) will be used.
    1. `./Restaurant <input-file-path>` <br/>
        >if you use this cmdLine, then you need to specify the input file path (relative/absolute). <br/>
        >if using relative path, the specify the path relative to '<SubmissionFolder>/src' folder.

### Input
You can specify input to the program using one of the two methods specified under 'Execution'. <br/>
1. If using Method 'a', default input file (/testfiles/input.txt) will be used.
2. If using Method 'b', you need to specify the input file path (relative/absolute).

### Output
1. Output file will be saved under the 'out' folder (<SubmissionFolder>/out/<inputFileName>.out).
1. Error log will be saved under the 'out' folder(<SubmissionFolder>/out/<inputFileName>.err).
1. Results will be displayed on the console.

### Folder Hierarchy
    <SubmissionFolder>                  Top level folder carmen zip submission
        <src>                           Folder containing a source code
            <main>
            <resource>
            <utility>
        <out>                           Folder where output will be stored
        <testfiles>                     Folder containing test files. Default test file is in this folder.




