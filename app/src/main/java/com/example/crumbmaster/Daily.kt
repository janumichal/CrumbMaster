package com.example.crumbmaster

data class Daily (val id: Int, val type: Int, val title: String, val goal: Int, var progress: Int, val points: Int, var active: Boolean,
                  var obtained: Boolean)
