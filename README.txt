Samantha Deshazer  project 2 cs458
This is checkers implemented with images
these images are stored in a bitmap stored in the project here:
boardgame\app\src\main\res\drawable-v24

originally I was drawing circles for the pieces, but you cannot remove a drawn image
so  I felt the need to use a bitmap. I tried setting pieces to the same color of the
background squares, but there were issues of overlaping. 

You also have to click a second time to king a piece, this is just because
the decision to king is set in the move functions. 

the board is drawn using drawRect, it is not an image. 
