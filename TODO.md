TODO
====

[ ] Bugs
    [X] Write to parameters are not done in the func frame, but in global?
    [X] ForEach loop: only first command is executed. e.g.:
        for i in [1] do
          print(i + "\n")
          print(2*i + "\n")
        end
    [ ] Single step: "Step over" springt aus funktionen raus

[ ] Console
    [X] Support ANSI codes for coloring (ECMA-48)
    [X] Support input                                                                                                                                                xx
    [X] "Clear" button
    [X] Show cursor only if it has the focus

[ ] Virtual Machine
    [ ] Virtual Machine Events
        [X] Execution started
        [X] Execution stopped
        [X] New Line Reached
        [X] New Event Reached

[ ] Execution/Debugging
    [X] Single step
    [X] Support pausing program
    [ ] Show variables
        [X] Show Variables
        [ ] Popup Dialog for Maps/Lists
        [ ] Allow user to set values     
        [ ] Distinguish between temporary and "system" variables
        [X] Show full call stack

[ ] IDE/Code Editor
    [ ] Show error messages in code
    [X] Show error messages in console
    [X] Rename tabs
    [X] Show "start executing program", "Program terminated" messages
    [ ] Export code to file
    [X] add "well know" style to stylesheets
    [X] smaller size of console when starting
    [ ] code completion

[ ] Sync
    [X] Connect to Dropbox
    [X] Bring up server on localhost when syncing with Dropbox

[ ] Runtime
    [X] implement "input"
    [X] only show cursor when input is pending
    [X] implement \n special codes
    [X] implement "len" function
    [X] implement math functions

[ ] Turtle
    [X] Slow mode that moves the Turtle pixel by pixel
    [ ] Support background images
    [X] Zoom buttons
    [X] Reset buttons
    [X] Make canvas draggable
    [X] implement "clear"
    [X] Implement "Double Buffering"

[ ] Manual
    [ ] explain turtle
    [ ] explain syntax
    [ ] explain input/output

[ ] Language
    [ ] Default parameters
    [X] range operator "..":  a..b == range(a,b)
    
