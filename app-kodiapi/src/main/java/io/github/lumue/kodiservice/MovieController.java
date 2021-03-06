package io.github.lumue.kodiservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.integration.channel.PublishSubscribeChannel;
import org.springframework.messaging.MessageHandler;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@CrossOrigin
public class MovieController {
	
	private final PublishSubscribeChannel currentMovieChangedChannel;
	
	private final KodiMovieService kodiMovieService;
	
	@Autowired
	public MovieController(PublishSubscribeChannel currentMovieChangedChannel, KodiMovieService kodiMovieService) {
		this.currentMovieChangedChannel = currentMovieChangedChannel;
		this.kodiMovieService = kodiMovieService;
	}
	
	@GetMapping(value = "/events", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
	public Flux<Movie> eventMessages() {
		return Flux.create(sink -> {
			MessageHandler handler = message -> {
				sink.next((Movie) message.getPayload());
			};
			sink.onCancel(() -> currentMovieChangedChannel.unsubscribe(handler));
			currentMovieChangedChannel.subscribe(handler);
		});
	}
	
	@PutMapping(value = "/movies/{movieId}/rating",consumes = MediaType.APPLICATION_JSON_VALUE)
	public Mono<Void> setMovieRating(@PathVariable Long movieId,@RequestBody Long rating ){
		return kodiMovieService.setRating(movieId,rating);
	}
	
	@GetMapping(value = "/movies/playing")
	public Mono<Movie> getPlaying( ){
		return kodiMovieService.getCurrentlyPlayingMovie();
	}
	
	@GetMapping(value = "/movies/tags",produces = MediaType.APPLICATION_JSON_VALUE)
	public Mono<List <String>> getTags( ){
		return kodiMovieService.getAllTags().collectList();
	}
	
	@GetMapping(value = "/movies/{movieId}/tags")
	public Mono<List<String>> getTags(@PathVariable Long movieId){
		return kodiMovieService.findById(movieId)
				.map(Movie::getTag)
				.flatMapMany(Flux::fromIterable).collectList();
				
	}
	
	@GetMapping(value = "/movies/{movieId}")
	public Mono<Movie> getMovie(@PathVariable Long movieId){
		return kodiMovieService.findById(movieId);
		
	}
	
	
	@GetMapping(value = "/movies")
	public Flux<Movie> getMovies(){
		return kodiMovieService.findAll();
		
	}
	
}
