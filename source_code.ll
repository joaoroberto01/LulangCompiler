{ teste procedimentos e funcoes aninhados recursivos OK
escreva as sucessivas somas de traz para frente
8
8
12
8
7
- 5
10 (FIM)
saida
2
2
20}
programa test;
var a,b,s: inteiro;

funcao soma: inteiro;
var c,a,som: inteiro;
    z: booleano;
procedimento ler;
inicio
    leia (c);
    leia (a);
    s:= soma;
    escreva (s);
fim;

procedimento loop;
var x: inteiro;
inicio
    leia (x); {para parar digite um valor maior que 9 }
    se x < 10
    entao ler;
fim;

inicio {corpo da funcao soma}
    loop;
    soma:= c+a;
fim;

inicio {corpo principal}
    s:= soma;
    escreva(s)
fim.