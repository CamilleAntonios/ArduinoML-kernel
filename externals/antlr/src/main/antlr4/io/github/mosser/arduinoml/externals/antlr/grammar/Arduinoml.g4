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
    action      :   receiver=IDENTIFIER '<=' value=signal ;
    transition  :   ('if' '(' expr ')' '=>' next=IDENTIFIER ) | ('if' expr '=>' next=IDENTIFIER ) ;
    initial     :   'INIT';

expr :  'not' '(' expr ')' |
        'not' expr |
        left=expr 'or' right=expr |
        left=expr 'xor' right=expr |
        left=expr 'and' right=expr |
        digitalEqualComp |
        analogComp |
        '(' expr ')' ;

digitalEqualComp : digitalSignal '==' digitalSignal ;
analogComp : analogSignal '>' analogSignal |
            analogSignal '>=' analogSignal |
            analogSignal '<' analogSignal |
            analogSignal '<=' analogSignal |
            analogSignal '>=' analogSignal ;


signal : digitalSignal | analogSignal ;
digitalSignal      :  DIGITAL_SIGNAL_CONST  | signalRead ;
analogSignal   :   ANALOG_SIGNAL_CONST | signalRead ;

signalRead : sensor_name=IDENTIFIER ;

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
