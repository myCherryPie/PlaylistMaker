package com.example.playlistmaker.data.dto

import com.example.playlistmaker.domain.models.Track

class SearchResponse (
   var results: List<TrackDto>
) : Response()