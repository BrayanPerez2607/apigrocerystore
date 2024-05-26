package com.supermercado.apigrocerystore.controllers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Arrays;
import java.util.List;

import com.supermercado.apigrocerystore.controller.ClientController;
import com.supermercado.apigrocerystore.model.Client;
import com.supermercado.apigrocerystore.service.ClientService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.junit.jupiter.api.extension.ExtendWith;
import com.fasterxml.jackson.databind.ObjectMapper;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(ClientController.class)
public class ClientControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ClientService clientService;

    @InjectMocks
    private ClientController clientController;

    private Client client1;
    private Client client2;
    private List<Client> clientList;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(clientController).build();

        client1 = new Client(1L, "John Doe", "johndoe@example.com", "3017580088", "Carrera 82A#104 DD 39", "JavaDeveloper2015", 1018226759L);
        client2 = new Client(2L, "Jane Doe", "janedoe@example.com", "3126935914", "Carrera 82A#104 DD 38", "jbcm2607", 43914653L);
        clientList = Arrays.asList(client1, client2);
    }

    @Test
    public void testGetAllClients() throws Exception {
        when(clientService.getAll()).thenReturn(clientList);

        mockMvc.perform(get("/apigrocerystore/clients"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].clientId").value(client1.getClientId()))
                .andExpect(jsonPath("$[1].clientId").value(client2.getClientId()));
    }

    @Test
    public void testGetById_Success() throws Exception {
        when(clientService.getByClientId(anyLong())).thenReturn(client1);

        mockMvc.perform(get("/apigrocerystore/clients/{clientId}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.clientId").value(client1.getClientId()));
    }

    @Test
    public void testGetById_NotFound() throws Exception {
        when(clientService.getByClientId(anyLong())).thenReturn(null);

        mockMvc.perform(get("/apigrocerystore/clients/{clientId}", 1L))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testCreateClient() throws Exception {
        when(clientService.addClient(any(Client.class))).thenReturn(client1);

        mockMvc.perform(post("/apigrocerystore/clients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(client1)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.clientId").value(client1.getClientId()));
    }

    @Test
    public void testUpdateClientById_Success() throws Exception {
        when(clientService.updateClient(anyLong(), any(Client.class))).thenReturn(client1);

        mockMvc.perform(put("/apigrocerystore/clients/{clientId}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(client1)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.clientId").value(client1.getClientId()));
    }

    @Test
    public void testUpdateClientById_NotFound() throws Exception {
        when(clientService.updateClient(anyLong(), any(Client.class))).thenReturn(null);

        mockMvc.perform(put("/apigrocerystore/clients/{clientId}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(client1)))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testDeleteClientById() throws Exception {
        mockMvc.perform(delete("/apigrocerystore/clients/{clientId}", 1L))
                .andExpect(status().isNoContent());
    }

    private static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}

