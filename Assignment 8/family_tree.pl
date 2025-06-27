% Gender facts
male(raj).
female(deepa).
male(arun).
female(priya).
male(sanjay).
female(isha).
male(kenji).
female(yuki).
male(amit).
female(neha).
male(ravi).
female(meera).

% Parent-child relationships
parent(raj, arun).
parent(deepa, arun).
parent(raj, priya).
parent(deepa, priya).

parent(arun, sanjay).
parent(arun, isha).
parent(priya, amit).
parent(priya, neha).

parent(kenji, raj).     % raj's father
parent(yuki, raj).      % raj's mother

parent(kenji, meera).   % meera is raj's sister
parent(yuki, meera).

% -----------------------
% Rules

% child(X, Y): X is child of Y
child(X, Y) :- parent(Y, X).

% grandparent(X, Y): X is grandparent of Y
grandparent(X, Y) :- parent(X, Z), parent(Z, Y).

% sibling(X, Y): X and Y share a parent, and are not the same person
sibling(X, Y) :- parent(Z, X), parent(Z, Y), X \= Y.

% cousin(X, Y): X and Y have parents who are siblings
cousin(X, Y) :- parent(A, X), parent(B, Y), sibling(A, B).

% descendant(X, Y): X is a descendant of Y
descendant(X, Y) :- parent(Y, X).
descendant(X, Y) :- parent(Y, Z), descendant(X, Z).

% ancestor(X, Y): X is an ancestor of Y
ancestor(X, Y) :- parent(X, Y).
ancestor(X, Y) :- parent(X, Z), ancestor(Z, Y).