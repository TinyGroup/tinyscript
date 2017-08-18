/*
TinyScript脚本描述
*/
grammar TinyScript;

//编译单元包含包定义、导入和类定义
compilationUnit
    :   packageDeclaration? importDeclaration*  classDeclaration? statementDeclaration
    ;
//定义包
packageDeclaration
    :   'package' qualifiedName ';'
    ;
//定义import
importDeclaration
    :   'import'  qualifiedName ('.' '*')? ';'
    ;

//定义执行定义
statementDeclaration
    :   statement*
    ;

//定义类
classDeclaration
    :   'class' Identifier
        classBody
    ;
//定义类体
classBody
    :   '{' memberDeclaration* '}'
    ;

//成员定义包含属性和方法
memberDeclaration
    :   methodDeclaration | fieldDeclaration
    ;

/* We use rule this even for void methods which cannot have [] after parameters.
   This simplifies grammar and we can consider void to be a type, which
   renders the [] matching as a context-sensitive issue or a semantic check
   for invalid return type after parsing.
 */
//方法为方法名加参数
methodDeclaration
    :   Identifier formalParameters
        (   methodBody
        )
    ;

//属性定义
fieldDeclaration
    :   variableDeclarators ';'
    ;


variableDeclarators
    :   variableDeclarator (',' variableDeclarator)*
    ;
//可以给属性赋初值
variableDeclarator
    :   Identifier ('=' expression)?
    ;


qualifiedNameList
    :   qualifiedName (',' qualifiedName)*
    ;

formalParameters
    :   '(' formalParameterList? ')'
    ;

formalParameterList
    :   formalParameter (',' formalParameter)*
    ;

formalParameter
    :    Identifier
    ;


methodBody
    :   block
    ;

constructorBody
    :   block
    ;

qualifiedName
    :   Identifier ('.' Identifier)*
    ;

literal
    :   IntegerLiteral
    |   FloatingPointLiteral
    |   CharacterLiteral
    |   StringLiteral
    |   BooleanLiteral
    |   NullLiteral
    ;



// STATEMENTS / BLOCKS

block
    :   '{' blockStatement* '}'
    ;

blockStatement
    :   localVariableDeclarationStatement
    |   statement
    ;

localVariableDeclarationStatement
    :    localVariableDeclaration ';'
    ;

localVariableDeclaration
    :    variableDeclarators
    ;

statement
    :   block                                                                       #statementBlock
    |   ifDirective  elseifDirective*  elseDirective?                               #if
    |   'for' '(' forControl ')' statement                                          #for
    |   'while' parExpression statement                                             #while
    |   'do' statement 'while' parExpression ';'                                    #do
    |   'switch' parExpression '{' caseSwitchBlock* defaultSwitchBlock? '}'         #switch
    |   'return' expression ';'                                                     #return
    |   'break' ('(' expression ')')? ';'                                           #break
    |   'continue' ('(' expression ')')? ';'                                        #continue
    |   ';'                                                                         #nullStatement
    |   statementExpression ';'                                                     #expressionStatement
    ;

ifDirective
    :   'if' parExpression statement 
    ;

elseifDirective
    :   'elseif' parExpression statement 
    ;
    
elseDirective
    :   'else' statement 
    ;

/** Matches cases then statements, both of which are mandatory.
 *  To handle empty cases at the end, we add switchLabel* to statement.
 */
 
caseSwitchBlock
    : 'case' constantExpression ':' blockStatement;
    
defaultSwitchBlock
    : 'default' ':' blockStatement;

forControl
    :   enhancedForControl
    |   forInit? ';' expression? ';' forUpdate?
    ;

forInit
    :   localVariableDeclaration
    |   expressionList
    ;

enhancedForControl
    :    Identifier ':' expression
    ;

forUpdate
    :   expressionList
    ;

// EXPRESSIONS

parExpression
    :   '(' expression ')'
    ;

expressionList
    :   expression (',' expression)*
    ;
    
expressionRange
    :   expression '..' expression 
    ;

statementExpression
    :   expression
    ;

constantExpression
    :   expression
    ;


lambdaParameters
	  :	Identifier '(' expressionList? ')'
	  |	Identifier
	  | '(' expressionList? ')'
	  ;
	
lambdaBody
	  :	expression
	  |	block
	  ;
	
expression
    :   primary                                                     #primaryExpression
    |   lambdaParameters '->' lambdaBody                            #lambdaExpression                               
    |   expression ('.') Identifier '(' expressionList? ')'         #functionCallExpression
    |   expression ('.') Identifier                                 #fieldAccessExpression
    |   Identifier '(' expressionList? ')'                          #functionCallExpression
    |   expression '[' expression ']'                               #arrayItemExpression
    |   '{' expressionList? '}'                                     #arrayExpression
    |   '{' mapEntryList? '}'                                       #mapExpression
    |   '[' (expressionList|expressionRange)? ']'                   #arrayListExpression
    |   creator                                                     #newExpression
    |   expression ('++' | '--')                                    #singleRightExpression
    |   ('++'|'--') expression                                      #singleLeftExpression
    |   ('~'|'!'|'+'|'-') expression                                #mathUnaryPrefixExpression
    |   expression ('*'|'/'|'%') expression                         #mathBinaryBasicExpression
    |   expression ('+'|'-') expression                             #mathBinaryBasicExpression
    |   expression ('<<' | '>>>' | '>>') expression                 #mathBinaryShiftExpression
    |   expression ('<=' | '>=' | '>' | '<') expression             #logicalCompareExpression
    |   expression ('==' | '!=') expression                         #logicalCompareExpression
    |   expression '&' expression                                   #mathBinaryBitwise
    |   expression '^' expression                                   #mathBinaryBitwise
    |   expression '|' expression                                   #mathBinaryBitwise
    |   expression '&&' expression                                  #logicalConnectExpression
    |   expression '||' expression                                  #logicalConnectExpression
    |   expression 'instanceof' expression                          #instanceofExpression
    |   expression '?' expression ':' expression                    #conditionalTernaryExpression
    |   Identifier? CUSTOM_SCRIPT                                    #customScriptExpression
    |   <assoc=right> expression
        (   '='
        |   '+='
        |   '-='
        |   '*='
        |   '/='
        |   '&='
        |   '|='
        |   '^='
        |   '>>='
        |   '>>>='
        |   '<<='
        |   '%='
        )
        expression                                                  #binaryRightExpression
    ;

primary
    :   '(' expression ')'
    |   THIS
    |   literal
    |   Identifier
    ;
    

mapEntryList
            :   entryPair (',' entryPair)*
            ;

entryPair
    :    expression ':' expression 
    ;          

creator
    :    'new' ( objectCreator | arrayCreator)
    ;
    
    
objectCreator
    :    qualifiedName '(' expressionList? ')'
    ;
    
arrayCreator
    :    qualifiedName  dims  arrayInitializer?
    ;
    
dims
	:	 dim+
	;
	
dim
	:	 '['expression? ']' 
	;
	
variableInitializer
    :   arrayInitializer
    |   expression
    ;
    
arrayInitializer
    :   '{' (variableInitializer (',' variableInitializer)* (',')? )? '}'
    ;


superSuffix
    :   arguments
    |   '.' Identifier arguments?
    ;

explicitGenericInvocationSuffix
    :   'super' superSuffix
    |   Identifier arguments
    ;

arguments
    :   '(' expressionList? ')'
    ;

// LEXER

// §3.9 Keywords

ASSERT        : 'assert';
BREAK         : 'break';
CASE          : 'case';
CLASS         : 'class';
CONTINUE      : 'continue';
DEFAULT       : 'default';
DO            : 'do';
ELSE          : 'else';
FOR           : 'for';
IF            : 'if';
IMPORT        : 'import';
PACKAGE       : 'package';
RETURN        : 'return';
SWITCH        : 'switch';
WHILE         : 'while';
THIS          : 'this';

// §3.10.1 Integer Literals

IntegerLiteral
    :   DecimalIntegerLiteral
    |   HexIntegerLiteral
    |   OctalIntegerLiteral
    |   BinaryIntegerLiteral
    ;

fragment
DecimalIntegerLiteral
    :   DecimalNumeral IntegerTypeSuffix?
    ;

fragment
HexIntegerLiteral
    :   HexNumeral IntegerTypeSuffix?
    ;

fragment
OctalIntegerLiteral
    :   OctalNumeral IntegerTypeSuffix?
    ;

fragment
BinaryIntegerLiteral
    :   BinaryNumeral IntegerTypeSuffix?
    ;

fragment
IntegerTypeSuffix
    :   [lL]
    ;

fragment
DecimalNumeral
    :   '0'
    |   NonZeroDigit (Digits? | Underscores Digits)
    ;

fragment
Digits
    :   Digit (DigitOrUnderscore* Digit)?
    ;

fragment
Digit
    :   '0'
    |   NonZeroDigit
    ;

fragment
NonZeroDigit
    :   [1-9]
    ;

fragment
DigitOrUnderscore
    :   Digit
    |   '_'
    ;

fragment
Underscores
    :   '_'+
    ;

fragment
HexNumeral
    :   '0' [xX] HexDigits
    ;

fragment
HexDigits
    :   HexDigit (HexDigitOrUnderscore* HexDigit)?
    ;

fragment
HexDigit
    :   [0-9a-fA-F]
    ;

fragment
HexDigitOrUnderscore
    :   HexDigit
    |   '_'
    ;

fragment
OctalNumeral
    :   '0' Underscores? OctalDigits
    ;

fragment
OctalDigits
    :   OctalDigit (OctalDigitOrUnderscore* OctalDigit)?
    ;

fragment
OctalDigit
    :   [0-7]
    ;

fragment
OctalDigitOrUnderscore
    :   OctalDigit
    |   '_'
    ;

fragment
BinaryNumeral
    :   '0' [bB] BinaryDigits
    ;

fragment
BinaryDigits
    :   BinaryDigit (BinaryDigitOrUnderscore* BinaryDigit)?
    ;

fragment
BinaryDigit
    :   [01]
    ;

fragment
BinaryDigitOrUnderscore
    :   BinaryDigit
    |   '_'
    ;

// §3.10.2 Floating-Point Literals

FloatingPointLiteral
    :   DecimalFloatingPointLiteral
    |   HexadecimalFloatingPointLiteral
    ;

fragment
DecimalFloatingPointLiteral
    :   Digits '.' Digits+ ExponentPart? FloatTypeSuffix?
    |   '.' Digits ExponentPart? FloatTypeSuffix?
    |   Digits ExponentPart FloatTypeSuffix?
    |   Digits FloatTypeSuffix
    ;

fragment
ExponentPart
    :   ExponentIndicator SignedInteger
    ;

fragment
ExponentIndicator
    :   [eE]
    ;

fragment
SignedInteger
    :   Sign? Digits
    ;

fragment
Sign
    :   [+-]
    ;

fragment
FloatTypeSuffix
    :   [fFdD]
    ;

fragment
HexadecimalFloatingPointLiteral
    :   HexSignificand BinaryExponent FloatTypeSuffix?
    ;

fragment
HexSignificand
    :   HexNumeral '.'?
    |   '0' [xX] HexDigits? '.' HexDigits
    ;

fragment
BinaryExponent
    :   BinaryExponentIndicator SignedInteger
    ;

fragment
BinaryExponentIndicator
    :   [pP]
    ;

// §3.10.3 Boolean Literals

BooleanLiteral
    :   'true'
    |   'false'
    ;

// §3.10.4 Character Literals

CharacterLiteral
    :   '\'' SingleCharacter '\''
    |   '\'' EscapeSequence '\''
    ;

fragment
SingleCharacter
    :   ~['\\]
    ;

// §3.10.5 String Literals

StringLiteral
    :   '"' StringCharacters? '"'
    ;

fragment
StringCharacters
    :   StringCharacter+
    ;


fragment
StringCharacter
    :   ~["\\]
    |   EscapeSequence
    ;

// §3.10.6 Escape Sequences for Character and String Literals

fragment
EscapeSequence
    :   '\\' [btnfr"'\\]
    |   OctalEscape
    |   UnicodeEscape
    ;

fragment
OctalEscape
    :   '\\' OctalDigit
    |   '\\' OctalDigit OctalDigit
    |   '\\' ZeroToThree OctalDigit OctalDigit
    ;

fragment
UnicodeEscape
    :   '\\' 'u' HexDigit HexDigit HexDigit HexDigit
    ;

fragment
ZeroToThree
    :   [0-3]
    ;

// §3.10.7 The Null Literal

NullLiteral
    :   'null'
    ;

// §3.11 Separators

LPAREN          : '(';
RPAREN          : ')';
LBRACE          : '{';
RBRACE          : '}';
LBRACK          : '[';
RBRACK          : ']';
SEMI            : ';';
COMMA           : ',';
DOT             : '.';

// §3.12 Operators

ASSIGN          : '=';
GT              : '>';
LT              : '<';
BANG            : '!';
TILDE           : '~';
QUESTION        : '?';
COLON           : ':';
EQUAL           : '==';
LE              : '<=';
GE              : '>=';
NOTEQUAL        : '!=';
AND             : '&&';
OR              : '||';
INC             : '++';
DEC             : '--';
ADD             : '+';
SUB             : '-';
MUL             : '*';
DIV             : '/';
BITAND          : '&';
BITOR           : '|';
CARET           : '^';
MOD             : '%';
ARROW           : '->';

ADD_ASSIGN      : '+=';
SUB_ASSIGN      : '-=';
MUL_ASSIGN      : '*=';
DIV_ASSIGN      : '/=';
AND_ASSIGN      : '&=';
OR_ASSIGN       : '|=';
XOR_ASSIGN      : '^=';
MOD_ASSIGN      : '%=';
LSHIFT_ASSIGN   : '<<=';
RSHIFT_ASSIGN   : '>>=';
URSHIFT_ASSIGN  : '>>>=';
CUSTOM_SCRIPT_START  : '[[';
CUSTOM_SCRIPT_END  : ']]';

// §3.8 Identifiers (must appear after all keywords in the grammar)

Identifier
    :   JavaLetter JavaLetterOrDigit*
    ;

fragment
JavaLetter
    :   [a-zA-Z$_] // these are the "java letters" below 0x7F
    |   // covers all characters above 0x7F which are not a surrogate
        ~[\u0000-\u007F\uD800-\uDBFF]
        {Character.isJavaIdentifierStart(_input.LA(-1))}?
    |   // covers UTF-16 surrogate pairs encodings for U+10000 to U+10FFFF
        [\uD800-\uDBFF] [\uDC00-\uDFFF]
        {Character.isJavaIdentifierStart(Character.toCodePoint((char)_input.LA(-2), (char)_input.LA(-1)))}?
    ;

fragment
JavaLetterOrDigit
    :   [a-zA-Z0-9$_] // these are the "java letters or digits" below 0x7F
    |   // covers all characters above 0x7F which are not a surrogate
        ~[\u0000-\u007F\uD800-\uDBFF]
        {Character.isJavaIdentifierPart(_input.LA(-1))}?
    |   // covers UTF-16 surrogate pairs encodings for U+10000 to U+10FFFF
        [\uD800-\uDBFF] [\uDC00-\uDFFF]
        {Character.isJavaIdentifierPart(Character.toCodePoint((char)_input.LA(-2), (char)_input.LA(-1)))}?
    ;

//
// Additional symbols not defined in the lexical specification
//
AT : '@';
RANGE : '..';
ELLIPSIS : '...';

//
// Whitespace and comments
//

WS  :  [ \t\r\n\u000C]+ -> skip
    ;

CUSTOM_SCRIPT
    :   ('[[') .*? (']]')
    ;

COMMENT
    :   '/*' .*? '*/' -> skip
    ;

LINE_COMMENT
    :   '//' ~[\r\n]* -> skip
    ;
