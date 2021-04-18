# Jetbrains Academy - Smart Calculator

My solutions for the Jetbrains Academy Problem Smart Calculator

https://hyperskill.org/projects/88

The solution is build up step by step over several stages. 
Stage 1 is the first and simple one. The following stages 
build up on the previous stages and get more and more advanced.
There are eight stages in total.

Because each stage is completely independent of the previous one,
IntelliJ might show some warnings about duplicated code between 
the stages.

## Stage 1/8

[click here to see description @ JetBrains Academy](https://hyperskill.org/projects/88/stages/486/implement)

We can add two numbers.

just execute this:

    gradle -PmainClass=stage1.MainKt run --console=plain
    
    1 9
    10

## Stage 2/8

[click here to see description @ JetBrains Academy](https://hyperskill.org/projects/88/stages/487/implement)

We add a loop. User has to input _/exit_ to leave program.

just execute this:

    gradle -PmainClass=stage2.MainKt run --console=plain

    3 8
    11

    /exit
    Bye!

## Stage 3/8

[click here to see description @ JetBrains Academy](https://hyperskill.org/projects/88/stages/488/implement)

We show a help message when the user enters _/help_.

just execute this:

    gradle -PmainClass=stage3.MainKt run --console=plain

    4 5 6
    15

    /help
    The program calculates the sum of numbers

## Stage 4/8

[click here to see description @ JetBrains Academy](https://hyperskill.org/projects/88/stages/489/implement)

We add minus operation and basic logic for repeated plus or minus signs.

just execute this:

    gradle -PmainClass=stage4.MainKt run --console=plain

    12 -- 3 +++ 5
    20

## Stage 5/8

[click here to see description @ JetBrains Academy](https://hyperskill.org/projects/88/stages/490/implement)

We add logic to recognize illegal expressions.

just execute this:

    gradle -PmainClass=stage5.MainKt run --console=plain
    
    23 + 4 - abc
    Invalid expression
    27
    abc
    Invalid expression

## Stage 6/8

[click here to see description @ JetBrains Academy](https://hyperskill.org/projects/88/stages/491/implement)

We add variables! For debug purposes enter _/v_ to show all variables.

just execute this:

    gradle -PmainClass=stage6.MainKt run --console=plain
    
    a=3
    b=5
    a + b
    8

    a
    3

    /v
    {a=3, b=5}

## Stage 7/8

[click here to see description @ JetBrains Academy](https://hyperskill.org/projects/88/stages/492/implement)

We add parenthesis and power operator ^! Internally we convert infix notation to reverse polish notation.

just execute this:

    gradle -PmainClass=stage7.MainKt run --console=plain
    
    a = 4
    b = 5
    c = 11
    a * ( c - b )
    24

    a ^ 3
    64

## Stage 8/8

[click here to see description @ JetBrains Academy](https://hyperskill.org/projects/88/stages/493/implement)

Now we use BigInteger, so we can deal with large numbers.

just execute this:

    gradle -PmainClass=stage8.MainKt run --console=plain
    
    a = 1234567890
    b = 1122345679012234567890
    a * b
    1385611936788751714665019052100
