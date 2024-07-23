//library to handle user input
import java.io.*;
import java.util.Scanner;

//PlayBoard Class implements the Othello game
// This class should be sufficient to handle all game logic
// Currently the display is handled by PrintBoard method. That should be moved to another class.
// We are locking down the myBoard to 8x8

public class PlayBoard{
  
  //A cell in the myBoard can be  "BLANK" or "BLACK" or "WHITE",
  public enum Color {BLACK,WHITE,BLANK};
  //CurrentPlayer keeps track of who plays next. Black starts
  private int CurrentPlayer = 0;
  private final static int BlackPlayer = 0;
  private final static int WhitePlayer = 1;
  

  //Creating two 2D arrays
  private Color[][] myBoard;
  private Color[][] tempBoard;
  //Constructor which creates an 8x8 Reversi Board with the correct coin setup.
  // 
  private final static int BSIZE = 8;
  
  
  public PlayBoard(){
    
    myBoard = new Color[BSIZE][BSIZE];
    tempBoard = new Color[BSIZE][BSIZE];
    //Initialize to blank
    for(int i = 0; i < BSIZE; i++){
      for(int j = 0; j < BSIZE; j++){
        myBoard[i][j] = Color.BLANK;
        tempBoard[i][j] = Color.BLANK;
      }
    }

    // Add two black and two white coins to start
    myBoard[(BSIZE/2)][(BSIZE/2)-1] = Color.BLACK;
    myBoard[(BSIZE/2)-1][(BSIZE/2)] = Color.BLACK;
    myBoard[(BSIZE/2)-1][(BSIZE/2)-1] = Color.WHITE;
    myBoard[(BSIZE/2)][(BSIZE/2)] = Color.WHITE;
    
    tempBoard[(BSIZE/2)][(BSIZE/2)-1] = Color.BLACK;
    tempBoard[(BSIZE/2)-1][(BSIZE/2)] = Color.BLACK;
    tempBoard[(BSIZE/2)-1][(BSIZE/2)-1] = Color.WHITE;
    tempBoard[(BSIZE/2)][(BSIZE/2)] = Color.WHITE;
  }
  
  // Returns Color of current player
  public Color GamePieceColor(){
    if (CurrentPlayer == BlackPlayer) return (Color.BLACK);
    else return Color.WHITE;
  }
  
  // REturns color of other player
  public Color OpponentColor(){
    if (CurrentPlayer == BlackPlayer) return (Color.WHITE);
    else return Color.BLACK;
  }
  
  
  private int lowerBound(int a, int b){
    if (a > b)
      return b;
    return a;
  }
  
  private int upperBound(int a, int b){
    if (a > b)
      return a;
    return b;
  }
  
             

  //This method checks if a move is valid. If it is, it returns true
  // DOES NOT MAKE THE MOVE
 
  public boolean isValidMove(int row, int column){
  
    //illegal move
    if (row < 0 || row > 7 || column < 0 || column > 7 || !(myBoard[row][column] == Color.BLANK)){
      return false;
    }
    
    Color UserColor = this.GamePieceColor();
    Color OpponentColor = this.OpponentColor();
      
      //Checking to see if the any coins flip along a row or column.
    for (int i = 0; i < BSIZE; i++){
      for (int j = 0; j < BSIZE; j++){
        int counter = 0; //how many opponent coins between two coins of player
        if (myBoard[i][j] == UserColor && !(row == i && column == j)){ //checks if a coin is of User's color.
          if (row == i && (int)(Math.abs(column - j)) > 1){ //checks if row is the same as placed coin; column space must also be > 1
            for (int k = this.lowerBound(column,j) + 1; k < this.upperBound(column,j); k++){ //counts no.of opponent's coins in between.
              if (myBoard[i][k] == OpponentColor)
                counter++;
            }
            if (counter == this.upperBound(column,j) - this.lowerBound(column,j) - 1)
              return true; //true if all coins in between belong to the opponent!
          }
          else if (column == j && (int)(Math.abs(row - i)) > 1){ //checks if column is the same as placed coin; row space must be > 1
            for (int k = this.lowerBound(row,i) + 1; k < this.upperBound(row, i); k++){ //counts no.of opponent's coins in between.
              if (myBoard[k][j] == OpponentColor)
                counter++;
            }
            if (counter == this.upperBound(row, i) - this.lowerBound(row,i) - 1)
              return true;  //true if all coins in between belong to the opponent!
          } 
          
          // The following if-statement checks for diagonal flips
          //checks if cell is in the same diagonal as placed coin; diagonal space must also be > 1
          else if (((int)(Math.abs(column - j))) == ((int)(Math.abs(row - i))) && ((int)(Math.abs(row - i))) > 1 && ((int)(Math.abs(column - j)) > 1)){ 
            int a = i;
            int b = j;
            
            while (row != a && column != b){
              if (row > i && column > j){ //Determining mode of iteration
               a++;
               b++;
               if (myBoard[a][b] == OpponentColor) //counts no.of opponent's coins in between.
                 counter++; 
              }
              else if (i > row && j > column){ //Determining mode of iteration
                a--;
                b--;
                if (myBoard[a][b] == OpponentColor) //counts no.of opponent's coins in between.
                  counter++; 
              }
              else if (i > row && column > j){ //Determining mode of iteration
                a--;
                b++;
               if (myBoard[a][b] == OpponentColor) //counts no.of opponent's coins in between.
                 counter++; 
              }
              else if (row > i && j > column){ //Determining mode of iteration
                a++;
                b--;
                if (myBoard[a][b] == OpponentColor) //counts no.of opponent's coins in between.
                  counter++; 
              }
               
              
              
              
            }
            
            if (counter == ((int)(Math.abs(row - i))) - 1)
              return true; //true if all coins in between belong to the opponent!
            
            
          }

        }
      }
    }
    
    return false; //false when no coins are flipped!
    
    
  }

    
    
  //Checks to see if there are any ValidPositions for Current Player.
    public boolean HasValidPositions(){
      for(int i = 0; i < BSIZE; i++){
        for(int j = 0; j < BSIZE; j++){
            if((this.isValidMove(i, j) == true)){
              return true;
            }
          }
        }
        
  
      return false;
    }
    
    //This method modifies myBoard with the next move isValidMove() is true .
    public boolean makeMove(int row, int column) {
      if (this.isValidMove(row, column) == true){
          this.flipCoins(row, column);
          myBoard[row][column] = this.GamePieceColor();
          tempBoard[row][column] = this.GamePieceColor();
          if (CurrentPlayer == BlackPlayer)
            CurrentPlayer = WhitePlayer;
          else
            CurrentPlayer = BlackPlayer;
          return true;
      }
      
      else
        System.out.println("You have played an illegal move. Please try again!");
      return false;
    }
    
    public void flipCoins(int row, int column) {
      //Move in every direction and flip all the coins

      Color UserColor = this.GamePieceColor();
      Color OpponentColor = this.OpponentColor();
      
      //Checking to see if the any coins flip along a row or column.
      for (int i = 0; i < BSIZE; i++){
        for (int j = 0; j < BSIZE; j++){
          boolean validFlip = true;
          int counter = 0; //how many opponent coins between two coins of player
          if (myBoard[i][j] == UserColor && !(row == i && column == j)){ //checks if a coin is of User's color.
            if (row == i && (int)(Math.abs(column - j)) > 1){ //checks if row is the same as placed coin; column space must also be > 1
              for (int k = this.lowerBound(column,j) + 1; k < this.upperBound(column,j); k++){ //counts no.of opponent's coins in between.
                if (myBoard[i][k] == OpponentColor){
                  counter++;
                  tempBoard[i][k] = UserColor;
                }
              }
              if ((counter != this.upperBound(column,j) - this.lowerBound(column,j) - 1)){
                validFlip = false;
                this.revertTempBoard();
              }
            }
            else if (column == j && (int)(Math.abs(row - i)) > 1){ //checks if column is the same as placed coin; row space must be > 1
              for (int k = this.lowerBound(row,i) + 1; k < this.upperBound(row, i); k++){ //counts no.of opponent's coins in between.
                if (myBoard[k][j] == OpponentColor){
                  counter++;
                  tempBoard[k][j] = UserColor;
                }
              }
              if ((counter != this.upperBound(row, i) - this.lowerBound(row,i) - 1)){
                validFlip = false;
                this.revertTempBoard();  
              }
            } 
            
            // The following if-statement checks for diagonal flips
            //checks if cell is in the same diagonal as placed coin; diagonal space must also be > 1
            else if (((int)(Math.abs(column - j))) == ((int)(Math.abs(row - i))) && ((int)(Math.abs(row - i))) > 1 && ((int)(Math.abs(column - j)) > 1)){ 
              int a = i;
              int b = j;
              
              while (row != a && column != b){
                if (row > i && column > j){ //Determining mode of iteration
                  a++;
                  b++;
                  if (myBoard[a][b] == OpponentColor){ //counts no.of opponent's coins in between.
                    counter++;
                    tempBoard[a][b] = UserColor;
                  }
                }
                else if (i > row && j > column){ //Determining mode of iteration
                  a--;
                  b--;
                  if (myBoard[a][b] == OpponentColor){ //counts no.of opponent's coins in between.
                    counter++;
                    tempBoard[a][b] = UserColor;
                  }
                }
                else if (i > row && column > j){ //Determining mode of iteration
                  a--;
                  b++;
                  if (myBoard[a][b] == OpponentColor){ //counts no.of opponent's coins in between.
                    counter++;
                    tempBoard[a][b] = UserColor;
                  }
                }
                else if (row > i && j > column){ //Determining mode of iteration
                  a++;
                  b--;
                  if (myBoard[a][b] == OpponentColor){ //counts no.of opponent's coins in between.
                    counter++;
                    tempBoard[a][b] = UserColor;
                  }
                  
                }
                
                    }
                              
              if ((counter != ((int)(Math.abs(row - i))) - 1)){
                validFlip = false;
                this.revertTempBoard();
              }
                  }
            if (validFlip == true){
              for (int e = 0; e < BSIZE; e++){
                for (int f = 0; f < BSIZE; f++){
                  myBoard[e][f] = tempBoard[e][f];
                }

              }
            }
          }
        }
              
        
              
              
      }
            
    }
 
    
    
    private void revertTempBoard(){
      for (int i = 0; i < BSIZE; i++){
        for (int j = 0; j < BSIZE; j++){
          tempBoard[i][j] = myBoard[i][j];
        }
      }
    }


    public void printBoard(){
      
      System.out.print("  A ");
      System.out.print("B ");
      System.out.print("C ");
      System.out.print("D ");
      System.out.print("E ");
      System.out.print("F ");
      System.out.print("G ");
      System.out.println("H ");
      int rowMarker = 0;
      for (int i = 0; i < BSIZE; i++){
        rowMarker++;
        for (int j = 0; j < BSIZE; j++){
          if (j==0){
            System.out.print(rowMarker + " "); 
          }
          
          if (j!= BSIZE -1){
            if (myBoard[i][j] == Color.BLACK){
              System.out.print("x "); 
            }
            else if(myBoard[i][j] == Color.WHITE){
              System.out.print("o ");
            }
            else
              System.out.print("- ");
          }
          else{
            if (j == BSIZE -1){
              if (myBoard[i][j] == Color.BLACK){
                System.out.println("x "); 
              }
              else if(myBoard[i][j] == Color.WHITE){
                System.out.println("o ");
              }
              else
                System.out.println("- ");
            }
          }
        }
      }
    }
    
    //This method actually runs the game.
    //The GameRun variable ends the game if both opponents must pass their turn due no legal moves remaining.
    public void playGame() throws Exception
    {
      //open the log file
      PrintWriter outFile = new PrintWriter(new File("log.txt"));
      int GameRun = 0;
      Scanner scan = new Scanner(System.in);
      System.out.println("Filename or Interactive(\"I\") if you want:");
      String inputMode = scan.nextLine(); 
     
      if (!inputMode.equals("I")){
        try{
           scan = new Scanner(new File(inputMode));
        }
        catch (FileNotFoundException e){
          System.out.println("Cannot read filename!");
          System.exit(1);
        }
       
      } 
      
      
      while (GameRun <= 1){

        GameRun = 0;
        if (this.HasValidPositions() == false){
          GameRun++;
          if (CurrentPlayer == WhitePlayer)
            CurrentPlayer = BlackPlayer;
          else
            CurrentPlayer = WhitePlayer;
          
          if(this.HasValidPositions() == false)
            GameRun++;
        }
        
        if (GameRun <= 1){
          this.printBoard();
          System.out.println("Enter the move:");
          String move = scan.nextLine();
          if (move.equals("q")) {
            outFile.println("q");
            outFile.close();
            System.exit(1);
          }
          int input_row = Integer.parseInt(String.valueOf(move.charAt(1)));
          int input_column = 0;
          
          if (move.charAt(0) == 'a')
            input_column = 1;
          else if (move.charAt(0) == 'b')
            input_column = 2;
          else if (move.charAt(0) == 'c')
            input_column = 3;
          else if (move.charAt(0) == 'd')
            input_column = 4;
          else if (move.charAt(0) == 'e')
            input_column = 5;
          else if (move.charAt(0) == 'f')
            input_column = 6;
          else if (move.charAt(0) == 'g')
            input_column = 7;
          else if (move.charAt(0) == 'h')
            input_column = 8;
          
          
          if (makeMove(input_row - 1, input_column - 1) == true){
           outFile.println(move);
          }
          
        }
        
      }
      //close the log file
      outFile.println("q");
      outFile.close();
    }
    
    
    //This method determines the winner by counting the number of Color.BLACK values to the no.of Color.WHITE values
    //in the myBoard 2D array.
    public String getWinner(){
      int num_Blacks = 0;
      int num_Whites = 0;
      for (int i = 0; i < BSIZE; i++){
        for (int j = 0; j < BSIZE; j++){
          if (myBoard[i][j] == Color.WHITE)
            num_Whites++;
          else if (myBoard[i][j] == Color.BLACK)
            num_Blacks++;
        }
        
      }

      if (num_Blacks == num_Whites)
        return ("The game is a TIE between White and Black!");
      if (num_Blacks > num_Whites)
        return ("The game is WON by Black!");
      if (num_Whites> num_Blacks)
        return("The game is WON by White!");
      
      return("ERROR");
        
    }

    
}
