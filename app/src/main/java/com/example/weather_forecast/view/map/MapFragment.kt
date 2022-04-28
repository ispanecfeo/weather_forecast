package com.example.weather_forecast.view.map

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.weather_forecast.R
import com.example.weather_forecast.databinding.FragmentMapBinding
import com.yandex.mapkit.Animation
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.*
import com.yandex.mapkit.map.Map
import com.yandex.mapkit.mapview.MapView
import com.yandex.mapkit.places.panorama.PanoramaService
import com.yandex.mapkit.search.*
import com.yandex.runtime.Error
import com.yandex.runtime.image.ImageProvider
import kotlin.properties.Delegates

const val defLat: Double = 55.751574
const val defLon: Double = 37.573856

class MapFragment :Fragment(), Session.SearchListener {

    private var _binding: FragmentMapBinding? = null
    private val binding get() = _binding!!
    private lateinit var mapView: MapView
    private var lat: Double = defLat
    private var lon: Double = defLon
    private lateinit var searchManager: SearchManager
    private lateinit var searchSession: Session

    companion object {
        const val BUNDLE_LAT = "map_lat"
        const val BUNDLE_LON = "map_lon"

        fun newInstance(bundel: Bundle): MapFragment {
            val fragment = MapFragment()
            fragment.arguments = bundel
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        MapKitFactory.initialize(requireContext())
        SearchFactory.initialize(requireContext());
        _binding = FragmentMapBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lat = arguments?.getDouble(BUNDLE_LAT) ?: defLat
        lon = arguments?.getDouble(BUNDLE_LON) ?: defLon

        searchManager = SearchFactory.getInstance().
            createSearchManager(SearchManagerType.COMBINED)

        mapView = binding.mapview
        mapView.map.move(
            CameraPosition(Point(lat, lon), 11.0f, 0.0f, 0.0f),
            Animation(Animation.Type.SMOOTH, 0F),
            null
        )

        binding.buttonSearch.setOnClickListener {
            submitQuery(binding.searchText.text.toString())
        }

        submitQuery()
    }

    fun submitQuery(searchText: String) {
        val searchOption = SearchOptions()
        searchOption.setSearchTypes(SearchType.GEO.value)
        searchSession = searchManager.submit(
            searchText,
            VisibleRegionUtils.toPolygon(binding.mapview.map.visibleRegion),
            searchOption,
            this
        )
    }

    fun submitQuery() {
        val searchOption = SearchOptions()
        searchOption.setSearchTypes(SearchType.GEO.value)
        searchSession = searchManager.submit(
            Point(lat, lon),
            16,
            searchOption,
            this
        )
    }

    override fun onStart() {
        super.onStart()
        mapView.onStart()
        MapKitFactory.getInstance().onStart()
    }

    override fun onStop() {
        mapView.onStop()
        MapKitFactory.getInstance().onStop()
        super.onStop()
    }

    override fun onSearchResponse(response: Response) {
        val mapObjects = mapView.map.mapObjects
        mapObjects.clear()

        binding.searchText.setText(response.collection.children.firstOrNull()?.obj
            ?.metadataContainer
            ?.getItem(ToponymObjectMetadata::class.java)
            ?.address
            ?.formattedAddress)


        val searchResult = response.collection.children.get(0)
        val point = searchResult.obj?.geometry?.get(0)?.point
        point?.let {
            mapObjects.addPlacemark(
                it,
                ImageProvider.fromResource(requireContext(), R.drawable.search_result)
            )

            mapView.map.move(
                CameraPosition(it, 11.0f, 0.0f, 0.0f),
                Animation(Animation.Type.SMOOTH, 0F),
                null
            )
        }
    }

    override fun onSearchError(p0: Error) {

    }

}