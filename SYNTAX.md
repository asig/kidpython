Syntax
======

```
program := stmt_block .

stmt_block := stmt { stmt } .

stmt := func_def
        | if_stmt
        | for_stmt
        | while_stmt
        | repeat_stmt
        | return_stmt
        | case_stmt
        | assignment_or_call .

func_def := "function" ident "(" opt_ident_list ")" func_body .

if_stmt := "if" expr "then" stmt_block
           { "else" "if" expr "then" stmt_block }
           [ "else" stmt_block ]
           "end".

for_stmt := "for" ident (
              "=" expr "to" expr ["step" expr]
              | "in" expr
            ) "do"
            stmt_block
            "end"

while_stmt := "while" expr "do"
              stmt_block
              "end"

assignment_or_call := ident { selector_or_call } [ "=" expr ].

repeat_stmt := "repeat" stmt_block "until" expr.

case_stmt := "case" expr "of" case { "|" case } "end".

case := case_label_list ":" stmt_block .

case_label_list := range_expr { "," range_expr }.

return_stmt := "return" [ expr ].

expr := and_expr { "and" and_expr }.

and_expr := or_expr { "or" or_expr }.

or_expr := range_expr [ relop range_expr ].

range_epxr := arith_expr [ ".." arith_expr ] .

arith_expr := term { ("+"|"-") term }.

term := factor { ("*"|"/") factor }.

factor := numlit
        | string_lit
        | list_lit
        | map_lit
        | ident { selector_or_call }
        | "function" "(" opt_ident_list ")" func_body .
        | "(" expr ")" { selector_or_call } .

opt_ident_list := [ ident { "," ident } ].

func_body := [stmt_block] "end" .

func_call := ident "(" [ expr { "," expr } ")" .

list_lit := "[" [expr { "," expr } "]".

map_lit := "{" [ map_entry { "," map_entry } "}".

map_entry := expr ":" expr .

selector_or_call := "[" expr "]"
                    | "." ident
                    | "(" [ expr { "," expr } ] ")" .

```
