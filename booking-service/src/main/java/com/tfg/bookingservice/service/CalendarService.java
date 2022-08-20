package com.tfg.bookingservice.service;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tfg.bookingservice.model.entity.Calendar;
import com.tfg.bookingservice.repository.CalendarRepository;

import reactor.core.publisher.Mono;

@Service
public class CalendarService {
	
	@Autowired
	CalendarRepository repository;
	
	public Mono<List<Calendar>> findAll() {
		Mono<List<Calendar>> result = Mono.just(repository.findAll());
		return result;
	}
	
	public Mono<Calendar> saveCalendar(Calendar calendar) {
		Mono<Calendar> result = Mono.just(repository.save(calendar));
		return result;
	}
	
    @Transactional
    public Mono<Optional<Calendar>> update(Long CalendarId, Calendar source) {
         return Mono.just(repository.findById(CalendarId).map(target -> {        	 
        	 target.setLunes(source.getLunes());
        	 target.setMartes(source.getMartes());
        	 target.setMiercoles(source.getMiercoles());
        	 target.setJueves(source.getJueves());
        	 target.setViernes(source.getViernes());
             return target; 
         }));
    }  
	
	public Mono<Boolean> existsById(Long id) {
		Mono<Boolean> result = Mono.just(repository.existsById(id));
		return result;
	}
	
	public Mono<Calendar> findById(Long id) {
		Optional<Calendar> Calendar = repository.findById(id);
		if (!Calendar.isEmpty())
			return Mono.just(Calendar.get());
		else return Mono.empty();
	}
	
	public Mono<Calendar> findByTeacherName(String teacherName) {
		Optional<List<Calendar>> Calendar = repository.findByTeacherName(teacherName);
		return Mono.just(Calendar)
				.map(data -> {
					if(Optional.ofNullable(data.get().size()).orElse(0) != 0)
						return data.get().get(0);
					else return new Calendar();
				});
	}

}
