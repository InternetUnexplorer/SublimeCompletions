# SublimeCompletions

A simple tool that generates ST3 completions from ComputerCraft help files, made for @viluon

1 - Download the jar file from [here](https://www.dropbox.com/s/wif4v32fjblubcp/SublimeCompletions.jar?dl=1) - *Note: You will need JRE 1.8 or later to run it*

2 - Use a tool such as WinRar or 7Zip to extract your ComputerCraft jar to a directory of your choosing.

3 - Set the `Input Directory` to `PATH_TO_EXTRACTED_JAR/assets/computercraft/lua/rom/help`

![](http://i.imgur.com/7UHsDO4.png)

4 - Set the `Output Directory` to wherever you want

5 - Press 'Start' and wait about a second.

6 - Profit! (The files will be in your output directory)

*Note: To edit the template that is used to generate the files, you can edit `/src/main/resources/template`.

The variable `%STUFF%` will be replaced with the completions, and `%APINAME%` will be replaced with the name of the API.

If you find any bugs, please let me know!
