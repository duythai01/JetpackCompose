package com.example.jetpackcomposeapp.presentation.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

/**
 * Dialog thông báo cơ bản có thể tùy chỉnh
 * 
 * @param showDialog Trạng thái hiển thị dialog
 * @param title Tiêu đề của dialog
 * @param message Nội dung thông báo
 * @param positiveButtonText Text của nút positive
 * @param negativeButtonText Text của nút negative (tùy chọn)
 * @param onPositiveClick Callback khi nhấn nút positive
 * @param onNegativeClick Callback khi nhấn nút negative
 * @param onDismiss Callback khi dismiss dialog
 */
@Composable
fun BaseAlertDialog(
    showDialog: Boolean,
    title: String,
    message: String,
    positiveButtonText: String = "OK",
    negativeButtonText: String? = null,
    onPositiveClick: () -> Unit = {},
    onNegativeClick: () -> Unit = {},
    onDismiss: () -> Unit = {}
) {
    if (showDialog) {
        AlertDialog(
            onDismissRequest = onDismiss,
            title = {
                Text(
                    text = title,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
            },
            text = {
                Text(
                    text = message,
                    fontSize = 14.sp,
                    color = Color.Gray
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        onPositiveClick()
                        onDismiss()
                    }
                ) {
                    Text(
                        text = positiveButtonText,
                        fontWeight = FontWeight.Medium
                    )
                }
            },
            dismissButton = {
                negativeButtonText?.let { buttonText ->
                    TextButton(
                        onClick = {
                            onNegativeClick()
                            onDismiss()
                        }
                    ) {
                        Text(
                            text = buttonText,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }
        )
    }
} 