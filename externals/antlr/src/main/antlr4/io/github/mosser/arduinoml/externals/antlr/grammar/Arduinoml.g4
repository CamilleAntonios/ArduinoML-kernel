grammar Arduinoml;


/******************
 ** Parser rules **
 ******************/

root            :   declaration bricks states EOF;

declaration     :   'application' name=IDENTIFIER;

bricks          :   (digitalSensor|analogSensor|digitalActuator|analogActuator)+;
    digitalSensor : sensor  'digital'
    analogSensor  : sensor  'analogical'
    digitalActuator  : actuator  'digital'
    analogActuator   : actuator  'analogical'
    sensor      :   'sensor'   location
    actuator    :   'actuator' location
    location    :   id=IDENTIFIER ':' port=PORT_NUMBER;

states          :   state+;
    state       :   initial? name=IDENTIFIER '{'  action+ transition+ '}';
    action      :   receiver=IDENTIFIER '<=' value=SIGNAL;
    transition  :   'if(' expression ')' '=>' next=IDENTIFIER ;
    initial     :   'INIT';

expression : (notExpression|digitEqualOperation|digitalBinaryOperation|analogBinaryOperation);
    notExpression : 'not' expression;
    digitEqualOperation : DIGITAL_SIGNAL '==' DIGITAL_SIGNAL
    digitalBinaryOperation : (andOp|orOp|xorOp);
    andOp : expression 'and'  expression;
    orOp  : expression  'or'  expression;
    xorOp : expression  'xor' expression;
    analogBinaryOperation : (biggerOp|biggerOrEqualOp|EqualOp);
    biggerOp : ANALOG_SIGNAL '>' ANALOG_SIGNAL;
    biggerOrEqual : ANALOG_SIGNAL '>=' ANALOG_SIGNAL;
    equalOp : ANALOG_SIGNAL '==' ANALOG_SIGNAL;


/*****************
 ** Lexer rules **
 *****************/

PORT_NUMBER     :   [1-9] | '11' | '12';
IDENTIFIER      :   LOWERCASE (LOWERCASE|UPPERCASE)+;
SIGNAL          :   ANALOG_SIGNAL | DIGITAL_SIGNAL ;
ANALOG_SIGNAL   :   ANALOG_SIGNAL_CONST | ANALOG_SIGNAL_READ ;
ANALOG_SIGNAL_CONST : [0-255] ;
ANALOG_SIGNAL_READ : analog_sensor=IDENTIFIER
DIGITAL_SIGNAL      :  DIGITAL_SIGNAL_CONST  | DIGITAL_SIGNAL_READ ;
DIGITAL_SIGNAL_READ : digital_sensor=IDENTIFIER
DIGITAL_SIGNAL_CONST : 'HIGH' | 'LOW' ;

/*************
 ** Helpers **
 *************/

fragment LOWERCASE  : [a-z];                                 // abstract rule, does not really exists
fragment UPPERCASE  : [A-Z];
NEWLINE             : ('\r'? '\n' | '\r')+      -> skip;
WS                  : ((' ' | '\t')+)           -> skip;     // who cares about whitespaces?
COMMENT             : '#' ~( '\r' | '\n' )*     -> skip;     // Single line comments, starting with a #
