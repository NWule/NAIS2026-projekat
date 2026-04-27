package com.example.RecommendedActivityService.service.impl;

import com.example.RecommendedActivityService.dto.PlayerDTO;
import com.example.RecommendedActivityService.model.Player;
import com.example.RecommendedActivityService.repository.PlayerRepository;
import com.example.RecommendedActivityService.service.IPlayerService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PlayerService implements IPlayerService {

    private final PlayerRepository playerRepository;

    public PlayerService(PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
    }

    @Override
    public PlayerDTO create(PlayerDTO dto) {
        Player player = mapToEntity(dto);
        return mapToDTO(playerRepository.save(player));
    }

    @Override
    public List<PlayerDTO> getAll() {
        return playerRepository.findAll()
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public PlayerDTO getById(Integer id) {
        return mapToDTO(getPlayerOrThrow(id));
    }

    @Override
    public PlayerDTO update(Integer id, PlayerDTO dto) {
        Player player = getPlayerOrThrow(id);

        player.setName(dto.getName());
        player.setSurname(dto.getSurname());
        player.setPosition(dto.getPosition());
        player.setDateOfBirth(dto.getDateOfBirth());
        player.setHeight(dto.getHeight());

        return mapToDTO(playerRepository.save(player));
    }

    @Override
    public void delete(Integer id) {
        getPlayerOrThrow(id); // validacija
        playerRepository.deleteById(id);
    }

    @Override
    public List<PlayerDTO> findByName(String name) {
        return playerRepository.findByName(name)
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<PlayerDTO> findBySurname(String surname) {
        return playerRepository.findBySurname(surname)
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public PlayerDTO findTopScorerAtHomeMatches() {
        Player player = playerRepository.findTopScorerAtHomeMatches();

        if (player == null) {
            throw new RuntimeException("No player found");
        }

        return mapToDTO(player);
    }

    @Override
    public PlayerDTO findMostRedCardsInLostMatches() {
        Player player = playerRepository.findPlayerWithMostRedCardsInLostMatches();

        if (player == null) {
            throw new RuntimeException("No player found");
        }

        return mapToDTO(player);
    }

    private Player getPlayerOrThrow(Integer id) {
        return playerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Player not found"));
    }

    private PlayerDTO mapToDTO(Player player) {
        PlayerDTO dto = new PlayerDTO();
        dto.setPlayerId(player.getPlayerId());
        dto.setName(player.getName());
        dto.setSurname(player.getSurname());
        dto.setPosition(player.getPosition());
        dto.setDateOfBirth(player.getDateOfBirth());
        dto.setHeight(player.getHeight());
        return dto;
    }

    private Player mapToEntity(PlayerDTO dto) {
        Player player = new Player();
        player.setPlayerId(dto.getPlayerId());
        player.setName(dto.getName());
        player.setSurname(dto.getSurname());
        player.setPosition(dto.getPosition());
        player.setDateOfBirth(dto.getDateOfBirth());
        player.setHeight(dto.getHeight());
        return player;
    }
}