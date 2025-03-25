package com.example.quizit_android_app.ui.social

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ChildFriendly
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.PendingActions
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material.icons.outlined.ArrowBackIosNew
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.quizit_android_app.R
import com.example.quizit_android_app.model.retrofit.DoneChallenges
import com.example.quizit_android_app.model.retrofit.Focus
import com.example.quizit_android_app.model.retrofit.OpenChallenges
import com.example.quizit_android_app.model.retrofit.Subject
import com.example.quizit_android_app.model.retrofit.User
import com.example.quizit_android_app.network.NetworkMonitor
import com.example.quizit_android_app.ui.home.OpenChallengeCard
import com.example.quizit_android_app.ui.home.ChallengeType
import com.example.quizit_android_app.ui.home.NoContentPlaceholder
import com.example.quizit_android_app.ui.home.NoInternetPlaceholder
import com.example.quizit_android_app.ui.play_quiz.quiz.ChallengeBottomSheet
import com.example.quizit_android_app.usecases.friendship.FriendshipStatus
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserDetailScreen(
    viewModel: UserDetailViewModel = hiltViewModel(),
    networkMonitor: NetworkMonitor = hiltViewModel(),
    onGoBack: () -> Unit,
    navigateToPlayChallenge: (OpenChallenges?) -> Unit,
    navigateToQuizDetail: (Subject?, Focus?) -> Unit
) {

    val user = viewModel.user.value
    val userStats = viewModel.userStats.value

    val isConnected = networkMonitor.isConnected

    val openChallenges = viewModel.openChallenges.value
    val doneChallenges = viewModel.doneChallenges.value
    val friendshipStatus = viewModel.friendshipStatus.value

    val isLoading = viewModel.isLoading.value

    val statisticsSheetState = androidx.compose.material.rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden, skipHalfExpanded = true)
    val coroutineScope = rememberCoroutineScope()

    val deleteFriendshipSheetState = rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden, skipHalfExpanded = true)

    var selectedChallenge by remember { mutableStateOf<OpenChallenges?>(null) }
    val challengeSheetState = androidx.compose.material.rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden,
        skipHalfExpanded = true
    )


    ModalBottomSheetLayout(
        sheetContent = {
            DeleteFriendshipBottomSheet(
                onClose = { coroutineScope.launch { deleteFriendshipSheetState.hide() } },
                onDelete = {
                    viewModel.removeFriendship()
                    coroutineScope.launch { deleteFriendshipSheetState.hide() }
                           },
                user = user
            )
        },
        sheetState = deleteFriendshipSheetState,
    ) {
        ModalBottomSheetLayout(
            sheetContent =  { StatisticsBottomSheet {
                coroutineScope.launch {
                    statisticsSheetState.hide()
                }
            } },
            sheetState = statisticsSheetState,
        ) {
            ModalBottomSheetLayout(
                sheetState = challengeSheetState,
                sheetContent = {
                    selectedChallenge.let { challenge ->
                        ChallengeBottomSheet(
                            onClose = { coroutineScope.launch { challengeSheetState.hide() } },
                            challenge = challenge,
                            onChallengeAccept = {
                                navigateToPlayChallenge(challenge)

                                coroutineScope.launch {
                                    challengeSheetState.hide()
                                }

                            },
                            onChallengeDecline = {
                                viewModel.declineChallenge(challenge?.challengeId!!)
                                coroutineScope.launch {
                                    challengeSheetState.hide()
                                }
                            }
                        )
                    } ?: Box(modifier = Modifier.size(1.dp))
                }
            ) {
                Scaffold(
                    topBar = {
                        SocialTopBar(onGoBack = { onGoBack() })
                    },
                    contentWindowInsets = WindowInsets(0.dp),
                    content = { paddingValues ->
                        if (isLoading) {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(paddingValues),
                                contentAlignment = Alignment.Center
                            ) {
                                CircularProgressIndicator(
                                    trackColor = Color.Gray
                                )
                            }
                        } else {

                            LazyColumn(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(paddingValues),
                                verticalArrangement = Arrangement.spacedBy(16.dp)
                            ) {
                                item {
                                    UserInfos(
                                        user = user,
                                        friendshipStatus = friendshipStatus,
                                        addFriend = { userId ->

                                            if(isConnected && userStats != null) {
                                                viewModel.addFriend(userId)

                                            }
                                                    },
                                        acceptFriendship = { viewModel.acceptFriendship() },
                                        removeFriendship = { coroutineScope.launch { deleteFriendshipSheetState.show() } }
                                    )
                                }

                                if(userStats == null) {
                                    item {
                                        NoInternetPlaceholder(id = R.drawable.internet_error_placeholder)
                                    }

                                } else {
                                    item {


                                        Box(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(horizontal = 16.dp)
                                        ) {
                                            StatisticsCard(
                                                onClick = { coroutineScope.launch { statisticsSheetState.show() } },
                                                stats = userStats
                                            )

                                        }

                                    }
                                    item {
                                        Spacer(modifier = Modifier.size(16.dp))
                                        Text(
                                            "Herausforderungen von ${
                                                user?.userFullname?.split(" ")?.first()
                                            }",
                                            style = MaterialTheme.typography.titleMedium,
                                            modifier = Modifier.padding(horizontal = 16.dp)
                                        )
                                    }

                                    if (openChallenges.isNotEmpty()) {
                                        item {
                                            LazyRow(
                                                modifier = Modifier.fillMaxWidth()
                                            ) {
                                                item {
                                                    Spacer(modifier = Modifier.size(16.dp))
                                                }
                                                items(openChallenges) { challenge ->


                                                    if (challenge.friendScore?.resultScore != null) {
                                                        OpenChallengeCard(
                                                            challenge = challenge,
                                                            type = ChallengeType.FRIEND,
                                                            onClick = {
                                                                selectedChallenge = challenge
                                                                coroutineScope.launch { challengeSheetState.show() }
                                                            },

                                                            )
                                                        Spacer(modifier = Modifier.size(16.dp))
                                                    }
                                                }
                                            }
                                        }
                                    } else {
                                        item {
                                            NoContentPlaceholder(id = R.drawable.no_open_challenges_placeholder)
                                        }
                                    }

                                    item {
                                        Spacer(modifier = Modifier.size(16.dp))
                                        Text(
                                            "Herausforderungen Historie",
                                            style = MaterialTheme.typography.titleMedium,
                                            modifier = Modifier.padding(horizontal = 16.dp)
                                        )
                                    }

                                    if (doneChallenges.isNotEmpty()) {
                                        item {
                                            LazyRow(
                                                modifier = Modifier.fillMaxWidth()
                                            ) {
                                                item {
                                                    Spacer(modifier = Modifier.size(16.dp))
                                                }
                                                items(doneChallenges.take(7)) { challenge ->
                                                    DoneChallengeCard(
                                                        challenge = challenge,
                                                        navigateToUserDetail = { id, user ->

                                                        },
                                                        navigateToQuizDetail = { subject, focus ->
                                                            navigateToQuizDetail(subject, focus)
                                                        }
                                                    )
                                                    Spacer(modifier = Modifier.size(16.dp))
                                                }
                                            }

                                            Spacer(modifier = Modifier.size(16.dp))
                                        }
                                    } else {
                                        item {
                                            NoContentPlaceholder(id = R.drawable.no_done_challenges_placeholder)
                                        }
                                    }

                                }


                            }
                        }
                    }
                )
            }

        }

    }
}

@Composable
fun DeleteFriendshipBottomSheet(onClose: () -> Unit, onDelete: () -> Unit, user: User?) {
    Box(
        modifier = Modifier
            .background(Color(0xFFEAF2FF))
            .fillMaxWidth()
            .padding(16.dp)

    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth(),
        ) {
            Text(
                "Freundschaft beenden",
                style = MaterialTheme.typography.titleLarge
            )
            Spacer(modifier = Modifier.size(16.dp))

            Text(
                "Möchtest du die Freundschaft mit ${user?.userFullname} wirklich beenden?",
                style = MaterialTheme.typography.bodySmall
            )
            Spacer(modifier = Modifier.size(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {

                Button(
                    onClick = { onClose() },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.White
                    ),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.weight(1f)

                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceAround

                    ) {
                        Text(
                            "Abbrechen",
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = androidx.compose.ui.text.font.FontWeight.Bold,
                            color = Color.Black
                        )

                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close",
                            tint = Color(0xFFFF3B30)
                        )
                    }

                }

                Spacer(modifier = Modifier.size(8.dp))

                Button(
                    onClick = { onDelete() },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.White
                    ),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.weight(1f)

                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceAround

                    ) {
                        Text(
                            "Freund entfernen",
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = androidx.compose.ui.text.font.FontWeight.Bold,
                            color = Color.Black
                        )

                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Check",
                            tint = Color(0xFF007AFF)
                        )
                    }

                }


            }
        }

    }

}


@Composable
fun UserInfos(user: User?, friendshipStatus: FriendshipStatus, addFriend: (Int) -> Unit, acceptFriendship: () -> Unit, removeFriendship: () -> Unit) {

    Column(
        Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .background(Color(0xFFEAF2FF), shape = CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "User Icon",
                    tint = Color(0xFFB4DBFF),
                    modifier = Modifier.size(60.dp)
                )
            }

            Text(
                user?.userFullname?:"",
                style = MaterialTheme.typography.titleLarge
            )
            Text(user?.userClass?:"", style = MaterialTheme.typography.bodyMedium)

            when (friendshipStatus) {
                FriendshipStatus.NONE -> {
                    Button(
                        onClick = { addFriend(user?.userId!!) },
                        modifier = Modifier
                            .width(200.dp)
                            .padding(8.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.White,
                            contentColor = Color.Black
                        ),
                        shape = RoundedCornerShape(16.dp),
                        border = BorderStroke(1.5.dp, Color.Black)
                    )
                    {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceAround,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.PersonAdd,
                                contentDescription = "Add",
                                modifier = Modifier
                                    .size(20.dp)
                            )

                            Text(
                                "anfreunden",
                                style = MaterialTheme.typography.titleMedium,
                            )

                        }
                    }

                }
                FriendshipStatus.FRIENDS -> {
                    Button(
                        onClick = { removeFriendship() },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF009DE0),
                            contentColor = Color.White
                        ),
                        modifier = Modifier
                            .width(200.dp)
                            .padding(8.dp),
                        shape = RoundedCornerShape(16.dp)

                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceAround,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = "Check",
                                modifier = Modifier
                                    .size(20.dp)
                            )


                            Text(
                                "befreundet",
                                style = MaterialTheme.typography.titleMedium,
                            )

                        }

                    }

                }
                FriendshipStatus.PENDING -> {
                    Button(
                        onClick = {  },
                        modifier = Modifier
                            .width(200.dp)
                            .padding(8.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFEAF2FF),
                            contentColor = Color.Black
                        ),
                        shape = RoundedCornerShape(16.dp),
                        border = BorderStroke(1.5.dp, Color.Black)

                    )
                    {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceAround,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.PendingActions,
                                contentDescription = "Wait",
                                modifier = Modifier
                                    .size(20.dp)
                            )


                            Text(
                                "ausstehend",
                                style = MaterialTheme.typography.titleMedium,
                            )

                        }
                    }

                }
                FriendshipStatus.PENDING_ACTIONREQ -> {
                    Button(
                        onClick = { acceptFriendship() },
                        modifier = Modifier
                            .width(200.dp)
                            .padding(8.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF0DE334),
                            contentColor = Color.White
                        ),
                        shape = RoundedCornerShape(16.dp),
                        border = BorderStroke(1.5.dp, Color.Black)

                    )
                    {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceAround,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.PendingActions,
                                contentDescription = "Accept",
                                modifier = Modifier
                                    .size(20.dp)
                            )


                            Text(
                                "annehmen",
                                style = MaterialTheme.typography.titleMedium,
                            )

                        }
                    }

                    Button(
                        onClick = { removeFriendship() },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFFF3B30),
                            contentColor = Color.White
                        ),
                        modifier = Modifier.size(200.dp),
                        shape = RoundedCornerShape(16.dp),
                        border = BorderStroke(1.5.dp, Color.Black)

                    )
                    {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceAround,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.PendingActions,
                                contentDescription = "Decline",
                                modifier = Modifier
                                    .size(20.dp)
                            )


                            Text(
                                "ablehnen",
                                style = MaterialTheme.typography.titleMedium,
                            )

                        }
                    }
                }

            }            }

        }

}

@Composable
fun DoneChallengeCard(challenge: DoneChallenges, navigateToUserDetail: (Int?, User) -> Unit, navigateToQuizDetail: (Subject?, Focus?) -> Unit) {
    Card(
        colors = CardDefaults.cardColors(containerColor = Color(0xFFEAF2FF)),
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier
            .width(300.dp)
            .clickable { navigateToQuizDetail(challenge.subject, challenge.focus) }

    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp, bottom = 16.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically

        ) {
            Box {
                CircularProgressIndicator(
                    progress = { (challenge.score?.resultScore!! / 100f).toFloat() },
                    color = Color(0xFF006FFD),
                    strokeWidth = 10.dp,
                    trackColor = Color(0xFFF4F3F6),
                    modifier = Modifier.size(80.dp)
                )

                Text(
                    text = "${(challenge.score?.resultScore?.roundToInt() ?: 0)}%",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Black,
                    modifier = Modifier.align(Alignment.Center)
                )
            }

            if (challenge.score?.resultScore!! >= challenge.friendScore?.resultScore!!) {
                Icon(
                    imageVector = Icons.Filled.EmojiEvents,
                    tint = Color(0xFFFF9913),
                    contentDescription = "Winner",
                    modifier = Modifier.size(45.dp)
                )
            } else {
                Icon(
                    imageVector = Icons.Filled.EmojiEvents,
                    tint = Color(0xFFC7C1BA),
                    modifier = Modifier.size(45.dp),
                    contentDescription = "Loser",
                )

            }

            Box {
                CircularProgressIndicator(
                    progress = { (challenge.friendScore?.resultScore!! / 100f).toFloat() },
                    color = Color(0xFFFB6E5C),
                    strokeWidth = 10.dp,
                    trackColor = Color(0xFFF4F3F6),
                    modifier = Modifier.size(80.dp)
                )

                Text(
                    text = "${(challenge.friendScore?.resultScore?.roundToInt() ?: 0)}%",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Black,
                    modifier = Modifier.align(Alignment.Center)
                )
            }


        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {

                val friendship = challenge.friendship
                val user = User(
                    userId = friendship?.friend?.userId,
                    userName = friendship?.friend?.userName,
                    userYear = friendship?.friend?.userYear,
                    userFullname = friendship?.friend?.userFullname,
                    userClass = friendship?.friend?.userClass,
                    userType = friendship?.friend?.userType,
                    userMail = friendship?.friend?.userMail,
                )
                Box(
                    modifier = Modifier
                        .size(50.dp)
                        .background(Color(0xFFEAF2FF), shape = CircleShape)
                        .clickable {
                            navigateToUserDetail(
                                challenge.friendship?.friendshipId,
                                user
                            )
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = "User Icon",
                        tint = Color(0xFFB4DBFF),
                        modifier = Modifier.size(70.dp)
                    )
                }

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.align(Alignment.CenterVertically)
                ) {
                    Text(
                        text = challenge.friendship?.friend?.userFullname ?: "",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.Black
                    )
                    Text(
                        text = challenge.focus?.focusName ?: challenge.subject?.subjectName ?: "",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Black
                    )
                }

                Box(
                    modifier = Modifier.size(50.dp),
                )
            }
        }

    }
}


@Composable
fun SocialTopBar(onGoBack: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        IconButton(
            onClick = { onGoBack() },
            modifier = Modifier.align(Alignment.CenterStart)
        ) {
            Icon(
                imageVector = Icons.Outlined.ArrowBackIosNew,
                contentDescription = "Back",
                tint = Color(0xFF8F9098)
            )
        }

        Text(
            text = "Social",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.align(Alignment.Center)
        )
    }

}