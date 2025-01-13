package com.example.quizit_android_app.ui.social

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.animateColor
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.ArrowBackIosNew
import androidx.compose.material.icons.outlined.Description
import androidx.compose.material.icons.outlined.StarBorder
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.VerticalDivider
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.util.fastForEachIndexed
import androidx.hilt.navigation.compose.hiltViewModel
import coil3.compose.AsyncImage
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun SocialScreen(
    viewModel: SocialViewModel = hiltViewModel(),
    navigateToUserDetail: (Int) -> Unit
) {

    val selectedTabIndex = viewModel.selectedTabIndex.value
    val friendships = viewModel.friendships.value
    val pendingFriendships = viewModel.pendingFriendships.value
    val results = viewModel.userResults.value

    val searchText = viewModel.searchText.value
    val filteredUsers = viewModel.filteredUsers.value

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
                navigateToUserDetail = {navigateToUserDetail(it)}
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
                Box(modifier = Modifier.padding(paddingValues)) {
                    when (selectedTabIndex) {
                        0 -> FriendsSection(
                            modifier = Modifier.fillMaxSize(),
                            friendships = friendships,
                            pendingFriendships = pendingFriendships,
                            navigateToUserDetail = { navigateToUserDetail(it) }
                        )
                        1 -> StatisticsSection(modifier = Modifier.fillMaxSize(), results = results)
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
        )
    }
}


@Composable
fun BottomSheetLayoutContent(
    onHide: () -> Unit,
    searchText: String,
    filteredUsers: List<User>,
    onUpdate: (String) -> Unit,
    navigateToUserDetail: (Int) -> Unit
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

        LazyColumn {
            items(filteredUsers) { user ->
                UserCard(user = user, navigateToUserDetail = { navigateToUserDetail(it) })

            }
        }

    }

}

@Composable
fun UserCard(user: User, navigateToUserDetail: (Int) -> Unit) {
    Row(
        modifier = Modifier
            .padding(start = 16.dp, end = 16.dp, top = 20.dp)
            .fillMaxWidth()
            .clickable { navigateToUserDetail(user.userId) },
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
                    modifier = Modifier.size(40.dp).offset(0.dp, 1.dp)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column {
                Text(
                    text = user.userFullname,
                    style = MaterialTheme.typography.titleSmall,
                )
                Spacer(modifier = Modifier.size(4.dp))
                Text(
                    text = user.userClass,
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
    friendships: List<Friendship>,
    pendingFriendships: List<PendingFriendship>,
    navigateToUserDetail: (Int) -> Unit
) {
    val scrollState = rememberScrollState()
    Column(
        modifier = modifier
            .fillMaxWidth()
            .verticalScroll(scrollState)
            //.padding(16.dp)
    ) {
        friendships.forEach { friendship ->
            FriendshipCard(friendship, navigateToUserDetail = { navigateToUserDetail(it) })
        }

        Spacer(modifier = Modifier.size(32.dp))

        Text("Freundschaftsanfragen", style = MaterialTheme.typography.titleMedium, modifier = Modifier.padding(horizontal = 16.dp))

        pendingFriendships.forEach { pendingFriendship ->
            PendingFriendshipCard(pendingFriendship = pendingFriendship, navigateToUserDetail = { navigateToUserDetail(it) })
        }

    }
}

@Composable
fun FriendshipCard(friendship: Friendship, navigateToUserDetail: (Int) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp, top = 20.dp)
            .clickable { navigateToUserDetail(friendship.friendId) },
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
                    modifier = Modifier.size(40.dp).offset(0.dp, 1.dp)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column {
                Text(
                    text = friendship.friendName,
                    style = MaterialTheme.typography.titleSmall,
                )
                Spacer(modifier = Modifier.size(4.dp))
                Text(
                    text = friendship.friendYear.toString(),
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
fun PendingFriendshipCard(pendingFriendship: PendingFriendship, navigateToUserDetail: (Int) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp, top = 16.dp)
            .clickable { navigateToUserDetail(pendingFriendship.friendId) },
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
                    modifier = Modifier.size(40.dp).offset(0.dp, 1.dp)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column {
                Text(
                    text = pendingFriendship.friendName,
                    style = MaterialTheme.typography.titleSmall,
                )
                Spacer(modifier = Modifier.size(4.dp))
                Text(
                    text = pendingFriendship.friendYear.toString(),
                    style = MaterialTheme.typography.labelLarge,
                    color = Color(0xFF71727A)
                )
            }
        }

        Row {
            IconButton(
                onClick = { // TODO
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
                onClick = { //TODO
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
fun StatisticsSection(modifier: Modifier, results: List<Result>) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        StatisticsCard()

        Spacer(modifier = Modifier.size(16.dp))
        Text("Quiz Historie", style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.size(16.dp))


        LazyRow {
            items(results) { result ->

                ResultCard(result = result)
            }
        }

    }

}

@Composable
fun StatisticsCard() {

    Card(
        colors = CardDefaults.cardColors(containerColor = Color(0xFF009DE0)),
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier
            .fillMaxWidth()
            .height(110.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceAround
        ){
            Column(
                modifier = Modifier.align(Alignment.CenterVertically),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    imageVector = Icons.Outlined.StarBorder,
                    contentDescription = "Star Icon",
                    modifier = Modifier.size(30.dp),
                )

                Text(
                    "Punkte",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.White.copy(alpha = 0.5f)
                )

                Text(
                    "590",
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
                modifier = Modifier.align(Alignment.CenterVertically),
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
                    "590",
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
                modifier = Modifier.align(Alignment.CenterVertically),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Icon(
                    imageVector = Icons.Outlined.Description,
                    contentDescription = "Star Icon",
                    modifier = Modifier.size(30.dp),
                )

                Text(
                    "Score",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.White.copy(alpha = 0.5f)
                )

                Text(
                    "590",
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
                    .aspectRatio(8f/3f)
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