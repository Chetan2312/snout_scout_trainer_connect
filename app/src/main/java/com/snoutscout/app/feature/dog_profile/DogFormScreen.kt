package com.snoutscout.app.feature.dog_profile

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.snoutscout.app.SnoutScoutApp
import com.snoutscout.app.core.ui.*
import com.snoutscout.app.data.model.DogProfile
import com.snoutscout.app.data.model.MockData
import java.util.UUID

@Composable
fun DogFormScreen(dogId: String?, onBack: () -> Unit) {
    val context = LocalContext.current
    val container = (context.applicationContext as SnoutScoutApp).container
    val viewModel: DogViewModel = viewModel(factory = DogViewModelFactory(container))

    var name by remember { mutableStateOf("") }
    var breed by remember { mutableStateOf("") }
    var age by remember { mutableStateOf("") }
    var weight by remember { mutableStateOf("") }
    var gender by remember { mutableStateOf("Male") }
    var vaccination by remember { mutableStateOf("Up to date") }
    var issues by remember { mutableStateOf(listOf<String>()) }
    var newIssue by remember { mutableStateOf("") }
    var medicalHistory by remember { mutableStateOf("") }

    LaunchedEffect(dogId) {
        if (dogId != null) {
            viewModel.getDogById(dogId)?.let { dog ->
                name = dog.name; breed = dog.breed; age = dog.age; weight = dog.weight
                gender = dog.gender; vaccination = dog.vaccination; issues = dog.issues
                medicalHistory = dog.medicalHistory
            }
        }
    }

    val canSave = name.isNotBlank() && breed.isNotBlank()
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    Scaffold(
        topBar = { SSTopBar(title = if (dogId == null) "Add Dog" else "Edit $name", onBack = onBack) },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(padding).verticalScroll(rememberScrollState()).padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            SSInput(name, { name = it }, "Dog Name *", placeholder = "Bruno")
            Text("Breed *", style = MaterialTheme.typography.titleSmall)
            FlowRow(horizontalArrangement = Arrangement.spacedBy(6.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                MockData.ALL_BREEDS.forEach { b ->
                    SSChip(b, breed == b, { breed = b })
                }
            }
            SSInput(age, { age = it }, "Age", placeholder = "3 years")
            SSInput(weight, { weight = it }, "Weight", placeholder = "34 kg")
            Text("Gender", style = MaterialTheme.typography.titleSmall)
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                listOf("Male", "Female").forEach { g ->
                    SSChip(g, gender == g, { gender = g })
                }
            }
            Text("Vaccination Status", style = MaterialTheme.typography.titleSmall)
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                listOf("Up to date", "Overdue", "Not started").forEach { v ->
                    SSChip(v, vaccination == v, { vaccination = v })
                }
            }
            Text("Behavioral Issues", style = MaterialTheme.typography.titleSmall)
            issues.forEach { issue ->
                AssistChip(
                    onClick = { issues = issues - issue },
                    label = { Text(issue) },
                    trailingIcon = { Icon(Icons.Outlined.Close, null, modifier = Modifier.size(16.dp)) }
                )
            }
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = newIssue, onValueChange = { newIssue = it },
                    placeholder = { Text("Add issue...") },
                    modifier = Modifier.weight(1f), singleLine = true,
                    shape = androidx.compose.foundation.shape.RoundedCornerShape(12.dp)
                )
                IconButton(onClick = { if (newIssue.isNotBlank()) { issues = issues + newIssue; newIssue = "" } }) {
                    Icon(Icons.Outlined.Add, "Add")
                }
            }
            OutlinedTextField(
                value = medicalHistory, onValueChange = { medicalHistory = it },
                label = { Text("Medical History") }, modifier = Modifier.fillMaxWidth(),
                minLines = 3, shape = androidx.compose.foundation.shape.RoundedCornerShape(12.dp)
            )
            SSButton(
                text = "Save",
                onClick = {
                    viewModel.saveDog(
                        DogProfile(dogId ?: UUID.randomUUID().toString(), name, breed, age, gender,
                            weight, vaccination, "", issues, medicalHistory, 0)
                    )
                    onBack()
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = canSave
            )
        }
    }
}
