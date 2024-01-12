package nl.nielsvanbruggen.videostreamingplatform.actor.controller;

import lombok.Builder;
import lombok.Data;
import nl.nielsvanbruggen.videostreamingplatform.actor.model.Actor;

import java.util.List;

@Data
@Builder
public class AllActorsGetResponse {
    List<Actor> allActors;
}
