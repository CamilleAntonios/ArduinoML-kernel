grammar Arduinoml;


/******************
 ** Parser rules **
 ******************/
root            :   declaration bricks states EOF;

declaration     :   'application' name=IDENTIFIER;
bricks          :   (digitalSensor|analogSensor|digitalActuator|analogActuator)+;
    digitalSensor : sensor  'digital' ;
    analogSensor  : sensor  'analogical' ;
    digitalActuator  : actuator  'digital' ;
    analogActuator   : actuator  'analogical' ;
    sensor      :   'sensor'   location ;
    actuator    :   'actuator' location ;
    location    :   id=IDENTIFIER ':' port=PORT_NUMBER;

states          :   state+;
    state       :   initial? name=IDENTIFIER '{'  action+ transition+ '}';
    action      :   (digitalAction|analogicalAction) ;
    digitalAction      :   receiver=IDENTIFIER '<=' value=digitalSignal ;
    analogicalAction   :   receiver=IDENTIFIER '<=' value=analogSignal  ;
    transition  :   'if' '(' expression ')' '=>' next=IDENTIFIER ;
    initial     :   'INIT';

expression : orExpr ;
    orExpr : xorExpr ('or' xorExpr)* ;
    xorExpr : andExpr ('xor' andExpr)* ;
    andExpr : unaryExpr ('and' unaryExpr)* ;
    unaryExpr : 'not' unaryExpr | digitalEqualComp | analogComp | '(' expression ')' ;

digitalEqualComp : digitalSignal '==' digitalSignal ;

analogComp : analogSignal '>' analogSignal |
            analogSignal '>=' analogSignal |
            analogSignal '<' analogSignal |
            analogSignal '<=' analogSignal |
            analogSignal '>=' analogSignal ;


digitalSignal      :  DIGITAL_SIGNAL_CONST  | digitalSignalRead ;
digitalSignalRead : digital_sensor=IDENTIFIER ;


analogSignal   :   ANALOG_SIGNAL_CONST | analogSignalRead ;
analogSignalRead : analog_sensor=IDENTIFIER ;

/*****************
 ** Lexer rules **
 *****************/

PORT_NUMBER     :   [1-9] | '11' | '12' ;
IDENTIFIER      :   LOWERCASE (LOWERCASE|UPPERCASE)+ ;
DIGITAL_SIGNAL_CONST : 'HIGH' | 'LOW' ;
ANALOG_SIGNAL_CONST : '0'
                    | [1-9] [0-9]?
                    | '1' [0-9] [0-9]
                    | '2' [0-4] [0-9]
                    |  '25' [0-5];

/*************
 ** Helpers **
 *************/

fragment LOWERCASE  : [a-z] ;                                 // abstract rule, does not really exists
fragment UPPERCASE  : [A-Z] ;
NEWLINE             : ('\r'? '\n' | '\r')+      -> skip ;
WS                  : ((' ' | '\t')+)           -> skip ;     // who cares about whitespaces?
COMMENT             : '#' ~( '\r' | '\n' )*     -> skip ;     // Single line comments, starting with a #
