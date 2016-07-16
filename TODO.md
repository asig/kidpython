TODO
====

[ ] Bugs
    [X] Write to parameters are not done in the func frame, but in global?
    [X] ForEach loop: only first command is executed. e.g.:
        for i in [1] do
          print(i + "\n")
          print(2*i + "\n")
        end

    
[ ] Console
    [X] Support ANSI codes for coloring (ECMA-48)
    [X] Support input                                                                                                                                                xx
    [X] "Clear" button
    [X] Show cursor only if it has the focus

[ ] Virtual Machine
    [ ] Virtual Machine Events
        [X] Execution started
        [X] Execution stopped
        [ ] New Line Reached
        [ ] New Event Reached

[ ] IDE/Code Editor
    [ ] Show error messages in code
    [X] Show error messages in console
    [ ] Rename tabs
    [X] Show "start executing program", "Program terminated" messages
    [ ] Single step
    [ ] Support pausing program
    [ ] Export code to file
    [X] add "well know" style to stylesheets
    [X] smaller size of console when starting

[ ] Runtime
    [X] implement "input"
    [X] only show cursor when input is pending
    [X] implement \n special codes
    [X] implement "len" function
    [ ] implement math functions

[ ] Turtle
    [X] Slow mode that moves the Turtle pixel by pixel
    [ ] Support background images
    [X] Zoom buttons
    [X] Reset buttons
    [X] Make canvas draggable
    [X] implement "clear"
    [X] Implement "Double Buffering"

[ ] Language
    [ ] Default parameters
    [ ] range operator "..":  a..b == range(a,b)
    
