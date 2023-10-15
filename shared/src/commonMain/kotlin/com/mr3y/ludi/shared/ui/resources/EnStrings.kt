package com.mr3y.ludi.shared.ui.resources

import cafe.adriel.lyricist.LyricistStrings

@LyricistStrings(languageTag = "en", default = true)
val EnLudiStrings = LudiStrings(
    on_boarding_games_page_title = "Tell us about your favourite games",
    on_boarding_genres_page_title = "What are your favourite game genres",
    genres_page_genre_on_state_desc = { "Unselect genre $it" },
    genres_page_genre_off_state_desc = { "Select genre $it" },
    games_page_clear_search_query_desc = "Clear search query",
    games_page_search_field_desc = "Search for a specific game",
    games_page_game_content_desc = { name, rate -> "$name, Rated: ${rate.roundToTwoDecimalDigits()} stars" },
    games_page_game_on_state_desc = { "Remove $it from your favourite games" },
    games_page_game_off_state_desc = { "Add $it to your favourite games" },
    data_sources_page_data_source_on_state_desc = { "Unfollow $it RSS feed" },
    data_sources_page_data_source_off_state_desc = { "Follow $it RSS feed" },
    on_boarding_data_sources_page_title = "Follow your favorite news sources",
    on_boarding_secondary_text = "You can always change that later",
    on_boarding_fab_state_continue = "Continue",
    on_boarding_fab_state_continue_state_desc = "Continue",
    on_boarding_fab_state_skip = "Skip",
    on_boarding_fab_state_skip_state_desc = "Skip Onboarding and navigate to home screen",
    on_boarding_fab_state_finish = "Finish",
    on_boarding_fab_state_finish_state_desc = "Save my preferences and finish Onboarding",
    games_page_selected_label = "Selected",
    games_page_suggestions_label = "Suggestions",
    discover_page_search_field_placeholder = "What are you looking for?",
    discover_page_search_field_content_description = "Search for a specific game",
    discover_page_filter_icon_content_description = "Apply some filters to your search query to fine tune the results",
    discover_page_game_not_rated_content_description = { name, genre, platforms -> "$name is $genre genre game, available for $platforms" },
    discover_page_game_rated_content_description = { name, genre, rate, platforms -> "$name is $genre genre game rated: ${rate.roundToTwoDecimalDigits()} stars, available for $platforms" },
    discover_page_filters_sheet_close_content_description = "Close filters sheet",
    filter_chip_on_state_desc = { "Unselect $it" },
    filter_chip_off_state_desc = { "Select $it" },
    news_page_filter_icon_content_description = "Customize news feed",
    news_page_article_content_description = { title, author, source -> "$title\nPublished by $author on $source" },
    news_page_new_release_content_description = { name, date -> "$name is expected to have a new release on $date" },
    deals_page_tab_on_state_desc = { "Currently you\'re on $it tab" },
    deals_page_tab_off_state_desc = { "Select $it tab" },
    deals_page_search_field_content_description = "Search for a specific deal or game",
    deals_page_deal_content_description = { name, newPrice, oldPrice, savings -> "Claim $name game at ${newPrice.roundToTwoDecimalDigits()} bucks instead of ${oldPrice.roundToTwoDecimalDigits()} and save ${savings.roundToTwoDecimalDigits()} percent" },
    deals_page_giveaway_content_description = { "Claim $it game for free for a limited time, enjoy the offer before it expires" },
    deals_page_filters_sheet_close_content_description = "Close filters sheet",
    deals_label = "Deals",
    giveaways_label = "Giveaways",
    close_filters = "Close",
    deals_search_filter_bar_placeholder = "Search for a specific deal",
    deals_filter_icon_content_description = "Show filters",
    settings_page_theme_on_state_desc = { "Currently app theme is set to $it" },
    settings_page_theme_off_state_desc = { "Select $it Theme" },
    settings_page_dynamic_colors_off_content_description = "Dynamic colors feature isn\'t available for your current Android OS version, Update to Android 12 or later to enable it.",
    settings_page_dynamic_colors_on_content_description = "Material You dynamic colors feature",
    settings_page_dynamic_colors_on_state_desc = "Disable Dynamic colors",
    settings_page_dynamic_colors_off_state_desc = "Enable Dynamic colors",
    settings_page_preference_content_description = { "Navigate to see your $it" },
    edit_preferences_page_confirm_button_content_description = "Save my preferences",
    edit_preferences_page_game_content_description = { "Remove $it from my favourites" },
    edit_preferences_page_genre_on_state_desc = { "Remove $it Genre from my favourites" },
    edit_preferences_page_genre_off_state_desc = { "Add $it Genre to my favourites" },
    now = "now",
    news_no_releases_to_show = "No New Releases to show",
    news_no_news_to_show = "No New News to show",
    news_no_reviews_to_show = "No New Reviews to show"
)
