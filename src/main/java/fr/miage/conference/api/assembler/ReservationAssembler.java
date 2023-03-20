package fr.miage.conference.api.assembler;

import fr.miage.conference.reservation.entity.Reservation;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Service;

import javax.ejb.Singleton;
import javax.ejb.Startup;

@Singleton
@Startup
@Service
public class ReservationAssembler implements RepresentationModelAssembler<Reservation, EntityModel<Reservation>> {
    @Override
    public EntityModel<Reservation> toModel(Reservation entity) {
        return EntityModel.of(entity);
    }

    @Override
    public CollectionModel<EntityModel<Reservation>> toCollectionModel(Iterable<? extends Reservation> entities) {
        return RepresentationModelAssembler.super.toCollectionModel(entities);
    }
}
