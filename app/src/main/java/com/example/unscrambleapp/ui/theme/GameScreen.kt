package com.example.unscrambleapp.ui.theme

import android.app.Activity
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.unscrambleapp.R
import com.example.unscrambleapp.ui.theme.ui.GameViewModel


@Composable
fun GameScreen(
 modifier: Modifier = Modifier,
 gameViewModel: GameViewModel = viewModel()
) {

 val gameUiState by gameViewModel.uiState.collectAsState();

 Column(
  modifier = Modifier
   .fillMaxSize()
   .padding(16.dp)
 ) {
  GameStatus(
   wordCount = gameUiState.currentWordCount,
   score = gameUiState.score
  );
  GameLayout(
   currentScrambledWord = gameUiState.currentScrambleWord,
   onKeyboardDone = { gameViewModel.checkUserGuess() },
   onUserGuessChanged = { gameViewModel.updateUserGuess(it) },
   userGuess = gameViewModel.userGuess,
   isGuessWrong = gameUiState.isGuessedWordWrong
  );
  Row(
   modifier = Modifier
    .fillMaxWidth()
    .padding(top = 16.dp)
  ) {
   OutlinedButton(
    onClick = { gameViewModel.skipWord() },
    modifier = Modifier
     .weight(1f)
     .padding(end = 8.dp)
   ) {
    Text(text = "Skip")
   }
   Button(
    onClick = { gameViewModel.checkUserGuess() },
    modifier = Modifier
     .weight(1f)
     .padding(start = 8.dp)
   ) {
    Text(text = "Submit")
   }
  }
 }

 if ( gameUiState.isGameOver) {
  FinalScoreDialog(
   score = gameUiState.score,
   onPlayAgain = { gameViewModel.resetGame() }
  )
 }
}

@Composable
fun GameStatus(
 wordCount: Int,
 score: Int,
 modifier: Modifier = Modifier
) {
 Row(
  modifier = Modifier
   .fillMaxWidth()
   .padding(16.dp)
   .size(48.dp)
 ) {
  Text(text = "$wordCount of 10 words")
  Text(
   text = "Score: $score",
   modifier = Modifier
    .fillMaxWidth()
    .wrapContentWidth(Alignment.End)
  );
 }
};

@Composable
fun GameLayout(
 modifier: Modifier = Modifier,
 currentScrambledWord: String,
 onKeyboardDone: () -> Unit,
 onUserGuessChanged: (String) -> Unit,
 userGuess: String,
 isGuessWrong: Boolean
) {
 Column(
  modifier = Modifier.fillMaxWidth(),
  verticalArrangement = Arrangement.spacedBy(24.dp)
 ) {
  Text(
   text = currentScrambledWord,
   modifier = Modifier.align(Alignment.CenterHorizontally),
   fontSize = 45.sp
  )
  Text(
   text = "Unscramble the word using all the letters",
   fontSize = 17.sp,
   modifier = Modifier.align(Alignment.CenterHorizontally)
  )
  OutlinedTextField(
   modifier = Modifier.fillMaxWidth(),
   value = userGuess,
   singleLine = true,
   onValueChange = onUserGuessChanged,
   label = {
//           if ( isGuessWrong ) {
//            Text(text = stringResource(id = ))
//           }
    Text(text = if (isGuessWrong) "Wrong Guess!" else "Enter your word")
   },
   isError = isGuessWrong,
   keyboardOptions = KeyboardOptions.Default.copy(
    imeAction = ImeAction.Done
   ),
   keyboardActions = KeyboardActions(
    onDone = { onKeyboardDone() }
   )
  )
 }
}

@Composable
private fun FinalScoreDialog(
 score: Int,
 onPlayAgain: () -> Unit,
 modifier: Modifier = Modifier
) {
 val activity = (LocalContext.current as Activity);

 AlertDialog(
  onDismissRequest = {
   /**
    * Dismiss the dialog when the user clicks outside the dialog or on the back
    * button. If you want to disable that functionality, simply use an empty
    * onCloseRequest.
    *
    **/
  },
  title = {
   Text(text = stringResource(id = R.string.congratulations))
  },
  text = {
   Text(text = stringResource(id = R.string.you_scored, score))
  },
  modifier = modifier,
  dismissButton = {
   TextButton(
    onClick = {
     activity.finish()
    }
   ) {
    Text(text = stringResource(id = R.string.exit))
   }
  },
  confirmButton = {
   TextButton(
    onClick = { onPlayAgain() }
   ) {
    Text(text = stringResource(id = R.string.play_again))
   }
  }
 )

}