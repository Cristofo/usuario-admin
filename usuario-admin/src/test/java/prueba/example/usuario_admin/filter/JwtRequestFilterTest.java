package prueba.example.usuario_admin.filter;


import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import prueba.example.usuario_admin.config.JwtTokenUtil;
import prueba.example.usuario_admin.service.MyUserDetailsService;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static org.mockito.Mockito.*;

class JwtRequestFilterTest {

    @Mock
    private MyUserDetailsService userDetailsService;

    @Mock
    private JwtTokenUtil jwtTokenUtil;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private FilterChain filterChain;

    @InjectMocks
    private JwtRequestFilter jwtRequestFilter;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        SecurityContextHolder.clearContext();
    }

    @Test
    void testDoFilterInternal_ValidToken() throws ServletException, IOException {
        
        String token = "validToken";
        String username = "user";
        UserDetails userDetails = mock(UserDetails.class);

        when(request.getHeader("Authorization")).thenReturn("Bearer " + token);
        when(jwtTokenUtil.getUsernameFromToken(token)).thenReturn(username);
        when(userDetailsService.loadUserByUsername(username)).thenReturn(userDetails);
        when(jwtTokenUtil.validateToken(token, userDetails)).thenReturn(true);

        
        jwtRequestFilter.doFilterInternal(request, response, filterChain);

        
        verify(filterChain).doFilter(request, response);
        verify(jwtTokenUtil).getUsernameFromToken(token);
        verify(userDetailsService).loadUserByUsername(username);
        verify(jwtTokenUtil).validateToken(token, userDetails);
    }

    @Test
    void testDoFilterInternal_InvalidToken() throws ServletException, IOException {
        
        String token = "invalidToken";

        when(request.getHeader("Authorization")).thenReturn("Bearer " + token);
        when(jwtTokenUtil.getUsernameFromToken(token)).thenThrow(new MalformedJwtException("Malformed token"));

        
        jwtRequestFilter.doFilterInternal(request, response, filterChain);

        
        verify(filterChain).doFilter(request, response);
        verify(jwtTokenUtil).getUsernameFromToken(token);
    }

    @Test
    void testDoFilterInternal_ExpiredToken() throws ServletException, IOException {
        
        String token = "expiredToken";

        when(request.getHeader("Authorization")).thenReturn("Bearer " + token);
        when(jwtTokenUtil.getUsernameFromToken(token)).thenThrow(new ExpiredJwtException(null, null, "Token expired"));

        
        jwtRequestFilter.doFilterInternal(request, response, filterChain);

        
        verify(filterChain).doFilter(request, response);
        verify(jwtTokenUtil).getUsernameFromToken(token);
    }

    @Test
    void testDoFilterInternal_UsuarioNoEncontrado() throws ServletException, IOException {
        
        String token = "validToken";
        String username = "user";

        when(request.getHeader("Authorization")).thenReturn("Bearer " + token);
        when(jwtTokenUtil.getUsernameFromToken(token)).thenReturn(username);
        when(userDetailsService.loadUserByUsername(username)).thenThrow(new UsernameNotFoundException("User not found"));

        
        jwtRequestFilter.doFilterInternal(request, response, filterChain);

        
        verify(filterChain).doFilter(request, response);
        verify(jwtTokenUtil).getUsernameFromToken(token);
        verify(userDetailsService).loadUserByUsername(username);
    }

    @Test
    void testDoFilterInternal_NoAuthorizationHeader() throws ServletException, IOException {
        
        when(request.getHeader("Authorization")).thenReturn(null);

        
        jwtRequestFilter.doFilterInternal(request, response, filterChain);

        
        verify(filterChain).doFilter(request, response);
        verify(jwtTokenUtil, never()).getUsernameFromToken(anyString());
    }
}