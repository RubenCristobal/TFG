package com.tfg.bookingservice.service;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tfg.bookingservice.model.entity.Booking;
import com.tfg.bookingservice.repository.BookingRepository;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class BookingService {
	
	@Autowired
	BookingRepository repository;
	
	public Flux<Booking> findAll() {
		return Mono.just(repository.findAll())
				.flatMapMany(Flux::fromIterable);
	}
	
	public Mono<Booking> saveBooking(Booking booking) {
		return Mono.just(repository.save(booking));
	}
	
    @Transactional
    public Mono<Optional<Booking>> update(Long bookingId, Booking source) {
         return Mono.just(repository.findById(bookingId).map(target -> {        	 
        	 target.setDate(source.getDate());
        	 target.setTime(source.getTime());
        	 target.setFormat(source.getFormat());
             target.setDescription(source.getDescription());
             return target; 
         }));
    }
    
    public Mono<Void> deleteById(Long id) {
    	repository.deleteById(id);
    	return  Mono.empty();
    }
	
	public Mono<Boolean> existsById(Long id) {
		return Mono.just(repository.existsById(id));
	}
	
	public Mono<Booking> findById(Long id) {
		Optional<Booking> booking = repository.findById(id);
		if (!booking.isEmpty())
			return Mono.just(booking.get());
		else return Mono.empty();
	}
	
	public Flux<Booking> findByStudentName(String studentName) {
		Optional<List<Booking>> booking = repository.findByStudentName(studentName);
		return Mono.just(booking.get()).flatMapMany(Flux::fromIterable);
	}
	
	public Mono<Booking> getNextBookByStudentName(String studentName) {
		Optional<Booking> booking = repository.findFirstByStudentNameOrderByDateAscTimeAsc(studentName);
		if (!booking.isEmpty())
			return Mono.just(booking.get());
		else return Mono.empty();
	}
	
	public Mono<Booking> getNextBookByTeacherName(String teacherName) {
		Optional<Booking> booking = repository.findFirstByTeacherNameOrderByDateAscTimeAsc(teacherName);
		if (!booking.isEmpty())
			return Mono.just(booking.get());
		else return Mono.empty();
	}
	
	public Mono<Long> countByStudentName(String studentName) {
		return Mono.just(repository.countByStudentName(studentName).get());
	}
	
	public Mono<Long> countByTeacherName(String teacherName) {
		return Mono.just(repository.countByTeacherName(teacherName).get());
	}
	
	public Flux<Booking> findByTeacherName(String teacherName) {
		Optional<List<Booking>> booking = repository.findByTeacherName(teacherName);
		return Mono.just(booking.get()).flatMapMany(Flux::fromIterable);
	}

}
