grammar Include;

includes  : (listing)+
          ;
listing   : 'package' packages ('recursive')? ('exclude' classes)?
          | 'class' classes
          ;
packages  : qualified '.*' (',' qualified '.*')*
          ;
classes   : qualified (',' qualified)*
          ;
qualified : STRING ('.' STRING)*
          ;

STRING    : [a-zA-Z0-9]+ ;
WS        : [ \t\r\n] -> skip ;
