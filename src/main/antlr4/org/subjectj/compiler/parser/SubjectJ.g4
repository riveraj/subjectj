grammar SubjectJ;

subjectj : include ;
include : 'include:' module+ ;
module : file | directory ;
file : name ;
directory : name '.*' 'recursive'? exclude? ;
exclude : 'exclude:' module+ ;
name : IDENTIFIER ('.' IDENTIFIER)* ;

IDENTIFIER : [a-zA-Z0-9]+ ;
WS : [ \t\r\n] -> skip ;
