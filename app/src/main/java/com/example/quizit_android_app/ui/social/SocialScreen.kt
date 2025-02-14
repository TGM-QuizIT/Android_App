package com.example.quizit_android_app.ui.social

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.SportsScore
import androidx.compose.material.icons.outlined.SportsScore
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastForEachIndexed
import androidx.compose.ui.window.Popup
import androidx.hilt.navigation.compose.hiltViewModel
import coil3.compose.AsyncImage
import com.example.quizit_android_app.model.retrofit.AcceptedFriendship
import com.example.quizit_android_app.model.retrofit.PendingFriendship
import com.example.quizit_android_app.model.retrofit.User
import com.example.quizit_android_app.model.retrofit.UserStatsResponse
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun SocialScreen(
    viewModel: SocialViewModel = hiltViewModel(),
    navigateToUserDetail: (Int?, User) -> Unit
) {

    val selectedTabIndex = viewModel.selectedTabIndex.value
    val friendships = viewModel.friendships.value
    val pendingFriendships = viewModel.pendingFriendship.value
    val results = viewModel.userResults.value

    val searchText = viewModel.searchText.value
    val filteredUsers = viewModel.filteredUsers.value

    val isLoading = viewModel.isLoading
    val isModalSheetLoading = viewModel.isModalSheetLoading

    val stats = viewModel.userStats.value

    val sheetState = androidx.compose.material.rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden, skipHalfExpanded = true)
    val coroutineScope = rememberCoroutineScope()

    ModalBottomSheetLayout(
        sheetState = sheetState,
        sheetContent = {


            BottomSheetLayoutContent(
                onHide = { coroutineScope.launch { sheetState.hide() } },
                searchText = searchText,
                filteredUsers = filteredUsers,
                onUpdate = { viewModel.updateSearchText(it) },
                navigateToUserDetail = { user ->
                    navigateToUserDetail(null, user)},
                isModalSheetLoading = isModalSheetLoading
            )



        }
    ) {
        Scaffold(
            contentWindowInsets = WindowInsets(0.dp),
            topBar = {
                Column {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 16.dp),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text("Social", textAlign = TextAlign.Center, style = MaterialTheme.typography.titleLarge, modifier = Modifier.weight(1f))
                    }

                    Spacer(modifier = Modifier.size(16.dp))
                    SegmentTabBar(
                        selectedTabIndex = selectedTabIndex,
                        onTabSelected = { viewModel.updateTabIndex(it) }
                    )
                }
            },
            content = { paddingValues ->

                Box(
                    modifier = Modifier.fillMaxSize()
                ) {
                    if(isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier.align(Alignment.Center),
                            trackColor = Color.Gray
                        )
                    } else {
                        Box(modifier = Modifier.padding(paddingValues).fillMaxSize()) {
                            when (selectedTabIndex) {
                                0 -> FriendsSection(
                                    modifier = Modifier.fillMaxSize(),
                                    friendships = friendships,
                                    pendingFriendships = pendingFriendships,
                                    navigateToUserDetail = { friendshipId, user ->
                                        navigateToUserDetail(friendshipId, user) },
                                    acceptFriendship = { isAccept, id ->
                                        viewModel.acceptFriendship(isAccept, id)
                                    }
                                )
                                1 -> StatisticsSection(modifier = Modifier.fillMaxSize(), results = results, stats = stats)
                            }

                            if (selectedTabIndex == 0) {
                                FloatingActionButton(
                                    onClick = {
                                        coroutineScope.launch {
                                            sheetState.show()
                                            viewModel.setUsers()
                                        }
                                    },
                                    modifier = Modifier
                                        .align(Alignment.BottomEnd)
                                        .padding(16.dp)
                                        .size(52.dp),
                                    shape = CircleShape,
                                    containerColor = Color(0xFF006FFD),
                                    contentColor = Color.White
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Add,
                                        contentDescription = "Add",
                                    )
                                }
                            }
                        }

                    }

                }

            }
        )
    }
}


@Composable
fun BottomSheetLayoutContent(
    onHide: () -> Unit,
    searchText: String,
    filteredUsers: List<User?>,
    onUpdate: (String) -> Unit,
    navigateToUserDetail: (User) -> Unit,
    isModalSheetLoading: Boolean
) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
    ) {

        Box(
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Social", style = MaterialTheme.typography.titleLarge, modifier = Modifier.align(Alignment.Center))
            IconButton(
                onClick = {onHide()},
                modifier = Modifier
                    .padding(8.dp)
                    .align(Alignment.CenterEnd)

            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Close",
                    tint = Color.Black,
                    )
            }

        }

        TextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            value = searchText,
            onValueChange =  { onUpdate(it) },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,  // Beispiel: Lupe als führendes Icon
                    contentDescription = "Search",
                    tint = Color(0xFF2F3036),
                    modifier = Modifier.size(20.dp)
                )
            },

            label = {
                Text(
                    "Suche",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Black
                )
            },

            colors = androidx.compose.material3.TextFieldDefaults.colors(
                unfocusedContainerColor = Color(0xFFEAF2FF),
                focusedContainerColor = Color(0xFFEAF2FF),
                unfocusedIndicatorColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent


            ),
            shape = RoundedCornerShape(8.dp),



            )

        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            if(isModalSheetLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center),
                    trackColor = Color.Gray
                )
            } else {


                LazyColumn {
                    items(filteredUsers) { user ->
                        UserCard(user = user, navigateToUserDetail = { user ->
                            navigateToUserDetail(user) }
                        )

                    }
                }

            }
            }
        }

}

@Composable
fun UserCard(user: User?, navigateToUserDetail: (User) -> Unit) {
    Row(
        modifier = Modifier
            .padding(start = 16.dp, end = 16.dp, top = 20.dp)
            .fillMaxWidth()
            .clickable { navigateToUserDetail(user!!) },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {

            Box(
                modifier = Modifier
                    .background(Color(0xFFEAF2FF), shape = RoundedCornerShape(16.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "User Icon",
                    tint = Color(0xFFB4DBFF),
                    modifier = Modifier
                        .size(40.dp)
                        .offset(0.dp, 1.dp)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column {
                Text(
                    text = user?.userFullname!!,
                    style = MaterialTheme.typography.titleSmall,
                )
                Spacer(modifier = Modifier.size(4.dp))
                Text(
                    text = user.userClass!!,
                    style = MaterialTheme.typography.labelLarge,
                    color = Color(0xFF71727A)
                )
            }

        }
        Box(
            modifier = Modifier.size(30.dp),
            contentAlignment = Alignment.Center
        ) {
            // TODO Friend Notifications
        }
    }
}

@Composable
fun SegmentTabBar(selectedTabIndex: Int, onTabSelected: (Int) -> Unit) {
    val tabs = listOf("Freunde", "Statistik")

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(40.dp)
            .padding(start = 16.dp, end = 16.dp)

    ) {
        SingleChoiceSegmentedButtonRow(
            modifier = Modifier
                .fillMaxWidth()
                .border(width = 1.dp, Color.Black, shape = RoundedCornerShape(8.dp))
                .height(38.dp),

            content = {
                tabs.fastForEachIndexed { index, title ->
                    Tab(
                        text = {
                            Text(
                                title,
                                style = MaterialTheme.typography.bodyMedium,
                                color = if (selectedTabIndex == index) Color.White
                                else Color.Black
                            )
                        },
                        selected = selectedTabIndex == index,
                        onClick = { onTabSelected(index) },
                        modifier = Modifier
                            .weight(1f)
                            .padding(3.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(
                                if (selectedTabIndex == index) Color(0xFF006FFD)
                                else Color.Transparent
                            )

                            .border(
                                0.dp,
                                color = Color.Transparent,
                                shape = RoundedCornerShape(16.dp)
                            )
                    )
                }
            }
        )
    }
}

@Composable
fun FriendsSection(
    modifier: Modifier,
    friendships: List<AcceptedFriendship>,
    pendingFriendships: List<PendingFriendship>,
    navigateToUserDetail: (Int?, User) -> Unit,
    acceptFriendship: (Boolean, Int) -> Unit
) {

    LazyColumn(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 16.dp, bottom = 16.dp)
    ) {

        items(friendships) { friendship ->
            FriendshipCard(friendship, navigateToUserDetail = { friendshipId, user ->
                navigateToUserDetail(friendshipId, user) }
            )

        }

        item {
            Spacer(modifier = Modifier.size(32.dp))

            Text("Freundschaftsanfragen", style = MaterialTheme.typography.titleMedium, modifier = Modifier.padding(horizontal = 16.dp))

        }

        items(pendingFriendships) { pendingFriendship ->
            PendingFriendshipCard(
                pendingFriendship = pendingFriendship,
                navigateToUserDetail = { friendshipId, user  ->
                navigateToUserDetail(friendshipId, user)
                                       },
                acceptFriendship = { acceptFriendship, id ->
                    acceptFriendship(acceptFriendship, id)
                })
        }


    }
}

@Composable
fun FriendshipCard(friendship: AcceptedFriendship, navigateToUserDetail: (Int?, User) -> Unit) {
    val user = User(
        userId = friendship.friend?.userId,
        userName = friendship.friend?.userName,
        userYear = friendship.friend?.userYear,
        userFullname = friendship.friend?.userFullname,
        userClass = friendship.friend?.userClass,
        userType = friendship.friend?.userType,
        userMail = friendship.friend?.userMail,
    )
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp, top = 24.dp)
            .clickable { navigateToUserDetail(friendship.friendshipId, user) },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {

            Box(
                modifier = Modifier
                    .background(Color(0xFFEAF2FF), shape = RoundedCornerShape(16.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "User Icon",
                    tint = Color(0xFFB4DBFF),
                    modifier = Modifier
                        .size(40.dp)
                        .offset(0.dp, 1.dp)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column {
                Text(
                    text = friendship.friend?.userFullname!!,
                    style = MaterialTheme.typography.titleSmall,
                )
                Spacer(modifier = Modifier.size(4.dp))
                Text(
                    text = friendship.friend?.userYear!!.toString()+"xHIT",
                    style = MaterialTheme.typography.labelLarge,
                    color = Color(0xFF71727A)
                )
            }

        }
        Box(
            modifier = Modifier.size(30.dp),
            contentAlignment = Alignment.Center
        ) {
            // TODO Friend Notifications
        }

        Spacer(modifier = Modifier.size(16.dp))


    }

}

@Composable
fun PendingFriendshipCard(pendingFriendship: PendingFriendship, navigateToUserDetail: (Int?, User) -> Unit, acceptFriendship: (Boolean, Int) -> Unit) {
    val user = User(
        userId = pendingFriendship.friend?.userId,
        userName = pendingFriendship.friend?.userName,
        userYear = pendingFriendship.friend?.userYear,
        userFullname = pendingFriendship.friend?.userFullname,
        userClass = pendingFriendship.friend?.userClass,
        userType = pendingFriendship.friend?.userType,
        userMail = pendingFriendship.friend?.userMail,
    )
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp, top = 24.dp)
            .clickable { navigateToUserDetail(pendingFriendship.friendshipId, user) },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {

        Row(verticalAlignment = Alignment.CenterVertically) {

            Box(
                modifier = Modifier
                    .background(Color(0xFFEAF2FF), shape = RoundedCornerShape(16.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "User Icon",
                    tint = Color(0xFFB4DBFF),
                    modifier = Modifier
                        .size(40.dp)
                        .offset(0.dp, 1.dp)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column {
                Text(
                    text = pendingFriendship.friend?.userFullname!!,
                    style = MaterialTheme.typography.titleSmall,
                )
                Spacer(modifier = Modifier.size(4.dp))
                Text(
                    text = pendingFriendship.friend?.userYear.toString()+"xHIT",
                    style = MaterialTheme.typography.labelLarge,
                    color = Color(0xFF71727A)
                )
            }
        }

        Row {
            IconButton(
                onClick = {
                    acceptFriendship(true, pendingFriendship.friendshipId!!)
                },
                modifier = Modifier
                    .background(Color(0xFF0DE334), shape = CircleShape)
                    .size(30.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = "Approve",
                    tint = Color.White
                )
            }

            Spacer(modifier = Modifier.width(16.dp))


            IconButton(
                onClick = {
                    acceptFriendship(false, pendingFriendship.friendshipId!!)
                },
                modifier = Modifier
                    .background(Color(0xFFFF3B30), shape = CircleShape)
                    .size(30.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Reject",
                    tint = Color.White
                )
            }
        }
    }
}
@Composable
fun StatisticsSection(modifier: Modifier, results: List<Result>,  stats: UserStatsResponse?) {

    var showPopup: Boolean by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(start = 16.dp, top = 16.dp, bottom = 16.dp, end = 16.dp)
    ) {
        if(showPopup) {
            StatisticsPopUp(onClose = { showPopup = false })
        }
        StatisticsCard(onClick = { showPopup = true }, stats = stats)

        Spacer(modifier = Modifier.size(16.dp))
        Text("Quiz Historie", style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.size(16.dp))


        LazyRow(
            modifier = Modifier.padding(PaddingValues(0.dp))
        ) {
            items(results) { result ->

                ResultCard(result = result)
            }
        }

    }

}

@Composable
fun StatisticsPopUp(onClose: () -> Unit) {
    Popup(
        onDismissRequest = onClose,
        alignment = Alignment.Center,
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .fillMaxSize()
                .wrapContentHeight()
                .background(color = Color(0xFFF8F9FE), shape = RoundedCornerShape(16.dp))
                .padding(16.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center

            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Statistiken Info",
                        style = MaterialTheme.typography.titleLarge,
                        color = Color.Black
                    )
                    IconButton(onClick = onClose) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close",
                            tint = Color.Black
                        )
                    }
                }

                Spacer(modifier = Modifier.size(16.dp))

                // Statistics Items
                StatisticsItem(
                    icon = Icons.Default.EmojiEvents, // Example icon
                    title = "Challenges",
                    description = "Der Prozentsatz der gewonnenen Herausforderungen."
                )
                Spacer(modifier = Modifier.size(12.dp))
                StatisticsItem(
                    icon = Icons.Default.School, // Example icon
                    title = "TGM -Level",
                    description = "Die Platzierung im Vergleich zu anderen Schüler:innen sortiert nach dem Durchschnittsscore."
                )
                Spacer(modifier = Modifier.size(12.dp))
                StatisticsItem(
                    icon = Icons.Default.SportsScore, // Example icon
                    title = "Score",
                    description = "Der Durchschnittsscore deiner abgeschlossenen Quizze."
                )
            }
        }
    }
}

@Composable
fun StatisticsItem(icon: ImageVector, title: String, description: String) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.fillMaxWidth()
        ){
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = Color(0xFFBAA7A7),
                modifier = Modifier.size(35.dp)
                    .align(Alignment.CenterVertically)
            )
            Spacer(modifier = Modifier.size(12.dp))
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                color = Color.Black,
                modifier = Modifier.align(Alignment.CenterVertically)
            )



        }
        Spacer(modifier = Modifier.size(4.dp))
        Text(
            text = description,
            style = MaterialTheme.typography.bodySmall,
            color = Color.Gray
        )
    }
}

@Composable
fun StatisticsCard(onClick: () -> Unit,  stats: UserStatsResponse?) {

    Card(
        colors = CardDefaults.cardColors(containerColor = Color(0xFF009DE0)),
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier
            .fillMaxWidth()
            .height(110.dp)
            .padding(end = 16.dp)
            .clickable { onClick() }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ){
            Column(
                modifier = Modifier
                    .weight(1f)
                    .align(Alignment.CenterVertically),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    imageVector = Icons.Default.EmojiEvents,
                    contentDescription = "Star Icon",
                    modifier = Modifier.size(30.dp),
                )

                Text(
                    "Challenges",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.White.copy(alpha = 0.5f)
                )

                Text(
                    stats?.stats?.winRate.toString()+"%",
                    style= MaterialTheme.typography.bodyMedium
                )

            }

            VerticalDivider(
                color = Color.White.copy(alpha = 0.5f),
                modifier = Modifier
                    .width(1.dp)
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                Color.Transparent,
                                Color.White.copy(alpha = 0.5f),
                                Color.Transparent
                            )
                        )
                    )

            )

            Column(
                modifier = Modifier
                    .weight(1f)
                    .align(Alignment.CenterVertically),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    imageVector = Icons.Filled.School,
                    contentDescription = "Star Icon",
                    modifier = Modifier.size(30.dp),
                )

                Text(
                    "TGM-Level",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.White.copy(alpha = 0.5f)
                )

                Text(
                    "#"+stats?.stats?.ranking.toString(),
                    style= MaterialTheme.typography.bodyMedium
                )
            }

            VerticalDivider(
                color = Color.White.copy(alpha = 0.5f),
                modifier = Modifier
                    .width(1.dp)
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                Color.Transparent,
                                Color.White.copy(alpha = 0.5f),
                                Color.Transparent
                            )
                        )
                    )

            )

            Column(
                modifier = Modifier
                    .weight(1f)
                    .align(Alignment.CenterVertically),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {

                Icon(
                    imageVector = Icons.Outlined.SportsScore,
                    contentDescription = "Score Icon",
                    modifier = Modifier.size(30.dp),
                )

                Text(
                    "Score",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.White.copy(alpha = 0.5f)
                )

                Text(
                    stats?.stats?.avgPoints.toString()+"%",
                    style= MaterialTheme.typography.bodyMedium
                )

            }
        }

    }

}

@Composable
fun ResultCard(result: Result) {
    Card(
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier
            .width(180.dp),
        colors = CardDefaults.cardColors(Color(0xFFF8F9FE))
    ) {
        Column {
            AsyncImage(
                model = "https://placehold.co/1600x600.png",
                contentDescription = "Result Focus",
                contentScale = ContentScale.FillBounds,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(8f / 3f)
            )

            Spacer(modifier = Modifier.size(8.dp))
            Text(
                "GGP - 2. Weltkrieg "+result.resultId,
                color = Color.Black,
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.align(Alignment.CenterHorizontally),
            )

            Spacer(modifier = Modifier.size(8.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(24.dp)
                    .padding(start = 16.dp, end = 16.dp)
                    .background(Color.White, shape = RoundedCornerShape(8.dp))
                    .border(1.dp, Color(0xFF006FFD), shape = RoundedCornerShape(8.dp)),
                contentAlignment = Alignment.CenterStart
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.8f) // 80% Fortschritt
                        .height(24.dp)
                        .background(Color(0xFF006FFD), shape = RoundedCornerShape(8.dp))
                )
                Text(
                    text = "80%",
                    color = Color.Black,
                    modifier = Modifier.align(Alignment.Center),
                    style = MaterialTheme.typography.labelLarge
                )
            }

            Spacer(modifier = Modifier.size(8.dp))
        }
    }
    Spacer(modifier = Modifier.size(8.dp))
}