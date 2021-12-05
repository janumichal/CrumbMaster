package com.example.crumbmaster

data class Statistics (val started: String,
                       var crumbs_total: Int,
                       var crumbs_today: Int,
                       var max_crumbs_per_day: Int,
                       var quests_total: Int,
                       var max_streets_per_day: Int,
                       var streets_today: Int)