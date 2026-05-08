package pt.isel.reversi.app.pages.settingsPage.sections

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.OutlinedButton
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import pt.isel.reversi.app.app.state.*
import pt.isel.reversi.app.pages.settingsPage.SettingsSection
import pt.isel.reversi.core.CoreConfig
import pt.isel.reversi.core.storage.GameStorageType

fun testTagCoreConfigSection() = "settings_core_config_section"
fun testTagStorageTypeButton() = "settings_storage_type_button"
fun testTagStorageTypeDropdown() = "settings_storage_type_dropdown"
fun testTagStorageTypeMenuItem(storageType: String) = "settings_storage_type_menuitem_$storageType"
fun testTagSavesPathTextField() = "settings_saves_path_textfield"
fun testTagDbURITextField() = "settings_db_uri_textfield"
fun testTagDbPortTextField() = "settings_db_port_textfield"
fun testTagDbNameTextField() = "settings_db_name_textfield"
fun testTagDbUserTextField() = "settings_db_user_textfield"
fun testTagDbPasswordTextField() = "settings_db_password_textfield"
fun testTagPopUpConfirmButton() = "settings_popup_confirm_button"

@Composable
fun ReversiScope.CoreConfigSection(
    coreConfig: CoreConfig,
    onConfigChange: (CoreConfig) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    var didClick by remember { mutableStateOf(false) }
    var accepted by remember { mutableStateOf(false) }

    @Suppress("AssignedValueIsNeverRead")
    if (didClick && !accepted) {
        ConfirmationPopUp(
            "Advanced user warning",
            "Changing storage settings may lead to data loss or corruption if not done correctly. Are you sure you want to proceed?",
            onConfirm = {
                accepted = true
                expanded = false
            },
            modifier = Modifier.testTag(testTagPopUpConfirmButton())
        )
    } else {
        SettingsSection(
            title = "Configuração do Jogo",
            modifier = Modifier.testTag(testTagCoreConfigSection())
        ) {
            // Storage Type Dropdown
            Box(modifier = Modifier.fillMaxWidth()) {
                OutlinedButton(
                    onClick = {
                        if (!didClick) didClick = true
                        else expanded = true
                    },
                    modifier = Modifier.fillMaxWidth().testTag(testTagStorageTypeButton()),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        ReversiText("Tipo de Armazenamento: ${coreConfig.gameStorageType.name}")
                    }
                }

                ReversiDropDownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                    modifier = Modifier.testTag(testTagStorageTypeDropdown())
                ) {
                    GameStorageType.entries.forEach { storageType ->
                        ReversiDropdownMenuItem(
                            text = storageType.name,
                            onClick = {
                                if (!didClick) didClick = true
                                else {
                                    onConfigChange(coreConfig.copy(gameStorageType = storageType))
                                    expanded = false
                                }
                            },
                            modifier = Modifier.testTag(testTagStorageTypeMenuItem(storageType.name))
                        )
                    }
                }
            }

            // File Storage Path (only show when FILE_STORAGE is selected)
            if (coreConfig.gameStorageType == GameStorageType.FILE_STORAGE) {
                ReversiTextField(
                    value = coreConfig.savesPath,
                    onValueChange = {
                        if (!didClick) didClick = true
                        else onConfigChange(coreConfig.copy(savesPath = it))
                    },
                    label = { ReversiText("Caminho das Gravações") },
                    modifier = Modifier.fillMaxWidth().testTag(testTagSavesPathTextField())
                )
            }

            // Database Settings (only show when DATABASE_STORAGE is selected)
            if (coreConfig.gameStorageType == GameStorageType.DATABASE_STORAGE) {
                ReversiTextField(
                    value = coreConfig.dbURI,
                    onValueChange = {
                        if (!didClick) didClick = true
                        else onConfigChange(coreConfig.copy(dbURI = it))
                    },
                    label = { ReversiText("URI do Banco de Dados") },
                    modifier = Modifier.fillMaxWidth().testTag(testTagDbURITextField())
                )

                ReversiTextField(
                    value = coreConfig.dbPort.toString(),
                    onValueChange = {
                        if (!didClick) didClick = true
                        else {
                            val newPort = it.toIntOrNull()
                            if (newPort != null && newPort > 0) {
                                onConfigChange(coreConfig.copy(dbPort = newPort))
                            }
                        }
                    },
                    label = { ReversiText("Porta do Banco de Dados") },
                    modifier = Modifier.fillMaxWidth().testTag(testTagDbPortTextField())
                )

                ReversiTextField(
                    value = coreConfig.dbName,
                    onValueChange = {
                        if (!didClick) didClick = true
                        else onConfigChange(coreConfig.copy(dbName = it))
                    },
                    label = { ReversiText("Nome do Banco de Dados") },
                    modifier = Modifier.fillMaxWidth().testTag(testTagDbNameTextField())
                )

                ReversiTextField(
                    value = coreConfig.dbUser,
                    onValueChange = {
                        if (!didClick) didClick = true
                        else onConfigChange(coreConfig.copy(dbUser = it))
                    },
                    label = { ReversiText("Usuário do Banco de Dados") },
                    modifier = Modifier.fillMaxWidth().testTag(testTagDbUserTextField())
                )

                ReversiTextField(
                    value = coreConfig.dbPassword,
                    onValueChange = {
                        if (!didClick) didClick = true
                        else onConfigChange(coreConfig.copy(dbPassword = it))
                    },
                    label = { ReversiText("Senha do Banco de Dados") },
                    modifier = Modifier.fillMaxWidth().testTag(testTagDbPasswordTextField())
                )
            }
        }
    }
}
