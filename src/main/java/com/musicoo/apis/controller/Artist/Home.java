package com.musicoo.apis.controller.Artist;

import com.musicoo.apis.model.Album;
import com.musicoo.apis.model.MusicooArtist;
import com.musicoo.apis.payload.request.AlbumReq;
import com.musicoo.apis.repository.ArtistRepo;
import com.musicoo.apis.service.Implementation.ArtistServicesImpl;
import com.musicoo.apis.service.jwt.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/api")
@RequiredArgsConstructor
public class Home {
    private final JwtUtil jwtUtil;
    private final ArtistRepo artistRepo;
    private final ArtistServicesImpl service;

    @PostMapping("/album/create")
    public ResponseEntity<?> createAlbum(@RequestBody AlbumReq albumReq, HttpServletRequest httpRequest) {
        String requestTokenHeader =httpRequest.getHeader("Authorization");
        String email = jwtUtil.getEmailFromToken(requestTokenHeader.substring(7));
        MusicooArtist artist = artistRepo.findByEmailIgnoreCase(email);
        return service.createAlbum(albumReq.name(), artist);
    }

    @GetMapping("/albums/view")
    public ResponseEntity<List<Album>> viewAllAlbumsOfArtist(@RequestParam("artistId") long aId) {
        return service.getAlbums(aId);
    }



}
