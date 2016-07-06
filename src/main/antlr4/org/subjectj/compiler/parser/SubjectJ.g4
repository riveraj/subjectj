grammar SubjectJ;

subject
    : include ;
include
    : 'include:' path+ ;
path
    : directory
    | directoryExclude
    | directoryRecursive
    | directoryRecursiveExclude
    | file ;
directory
    : name '.*' ;
directoryExclude
    : name '.*' exclude ;
directoryRecursive
    : name '.*' 'recursive' ;
directoryRecursiveExclude
    : name '.*' 'recursive' exclude ;
file
    : name ;
exclude
    : 'exclude:' (file | directory)+ ;
name
    : IDENTIFIER ('.' IDENTIFIER)* ;

IDENTIFIER : [a-zA-Z0-9]+ ;
WS : [ \t\r\n] -> skip ;
