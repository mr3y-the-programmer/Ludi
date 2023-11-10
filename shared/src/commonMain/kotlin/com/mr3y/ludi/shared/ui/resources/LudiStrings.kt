package com.mr3y.ludi.shared.ui.resources

data class LudiStrings(
    val on_boarding_games_page_title: String,
    val on_boarding_genres_page_title: String,
    val genres_page_genre_on_state_desc: (String) -> String,
    val genres_page_genre_off_state_desc: (String) -> String,
    val games_page_clear_search_query_desc: String,
    val games_page_search_field_desc: String,
    val games_page_game_content_desc: (name: String, rate: Float) -> String,
    val games_page_game_on_state_desc: (String) -> String,
    val games_page_game_off_state_desc: (String) -> String,
    val data_sources_page_data_source_on_state_desc: (String) -> String,
    val data_sources_page_data_source_off_state_desc: (String) -> String,
    val on_boarding_data_sources_page_title: String,
    val on_boarding_secondary_text: String,
    val on_boarding_fab_state_continue: String,
    val on_boarding_fab_state_continue_state_desc: String,
    val on_boarding_fab_state_skip: String,
    val on_boarding_fab_state_skip_state_desc: String,
    val on_boarding_fab_state_finish: String,
    val on_boarding_fab_state_finish_state_desc: String,
    val games_page_selected_label: String,
    val games_page_suggestions_label: String,
    val discover_page_search_field_placeholder: String,
    val discover_page_search_field_content_description: String,
    val discover_page_filter_icon_content_description: String,
    val discover_page_game_not_rated_content_description: (name: String, genre: String, platforms: String) -> String,
    val discover_page_game_rated_content_description: (name: String, genre: String, rate: Float, platforms: String) -> String,
    val discover_page_filters_sheet_close_content_description: String,
    val filter_chip_on_state_desc: (String) -> String,
    val filter_chip_off_state_desc: (String) -> String,
    val news_page_search_bar_placeholder: String,
    val news_page_search_bar_content_description: String,
    val news_page_filter_icon_content_description: String,
    val news_page_article_content_description: (title: String, author: String, source: String) -> String,
    val news_page_new_release_content_description: (name: String, date: String) -> String,
    val deals_page_tab_on_state_desc: (String) -> String,
    val deals_page_tab_off_state_desc: (String) -> String,
    val deals_page_search_field_content_description: String,
    val deals_page_deal_content_description: (name: String, newPrice: Float, oldPrice: Float, savings: Float) -> String,
    val deals_page_giveaway_content_description: (String) -> String,
    val deals_page_filters_sheet_close_content_description: String,
    val deals_label: String,
    val giveaways_label: String,
    val close_filters: String,
    val deals_search_filter_bar_placeholder: String,
    val deals_filter_icon_content_description: String,
    val settings_page_theme_on_state_desc: (String) -> String,
    val settings_page_theme_off_state_desc: (String) -> String,
    val settings_page_dynamic_colors_off_content_description: String,
    val settings_page_dynamic_colors_on_content_description: String,
    val settings_page_dynamic_colors_on_state_desc: String,
    val settings_page_dynamic_colors_off_state_desc: String,
    val settings_page_preference_content_description: (String) -> String,
    val edit_preferences_page_confirm_button_content_description: String,
    val edit_preferences_page_game_content_description: (String) -> String,
    val edit_preferences_page_genre_on_state_desc: (String) -> String,
    val edit_preferences_page_genre_off_state_desc: (String) -> String,
    val click_to_refresh: String,
    val now: String,
    val news_no_releases_to_show: String,
    val news_no_news_to_show: String,
    val news_no_reviews_to_show: String
)
