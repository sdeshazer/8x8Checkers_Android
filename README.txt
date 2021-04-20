Samantha Deshazer  project 2 cs458
This is a simple game of 2 player (no ai) checkers made for android.

piece images are stored in a bitmap stored in the project here:
boardgame\app\src\main\res\drawable-v24

originally I was drawing circles for the pieces, but you cannot remove a drawn image
so  I felt the need to use a bitmap. I tried setting pieces to the same color of the
background squares, but there were issues of overlaping. 

You also have to click a second time to king a piece, this is just because
the decision to king is set in the move functions. 

The board is drawn using drawRect(), it is not an image.

Bugs (TODO): 
Player cannot implement a double jump in one turn.
Player can increase score by moving a piece once match has ended.

To Add:
Possibiliy of adding a double-king and ultra-king. 
