package org.sas.seguridad.service;

import com.github.javaparser.utils.Utils;
import org.sas.seguridad.dto.RespuestaWs;
import org.sas.seguridad.entity.Menu;
import org.sas.seguridad.entity.MenuRol;
import org.sas.seguridad.entity.MenuTipo;
//import org.ibarra.seguridad.repository.*;
import org.sas.seguridad.repository.*;
import org.sas.seguridad.util.Constantes;
import org.sas.seguridad.util.model.BusquedaDinamica;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class MenuService {
    private static final Logger logger = Logger.getLogger(MenuService.class.getName());
    @Autowired
    private PersistableRepository dynamicRepo;
    @Autowired
    private RolRepo rolRepo;
    @Autowired
    private MenuRepo _menuRepo;
    @Autowired
    private MenuTipoRepo _menuTipoRepo;
    @Autowired
    private MenuRolRepo _menuRolRepo;

    @Autowired
    private UsuarioRolService _usuarioRolService;

//    @Autowired
//    private RedisConnectionChecker checkerRedis;

    @Caching(evict = {
            @CacheEvict(value = Constantes.cacheMenus, allEntries = true),
            @CacheEvict(value = Constantes.cacheMenusXusuario, allEntries = true),
            @CacheEvict(value = Constantes.cacheUsuarioInicioSesion, allEntries = true),
            @CacheEvict(value = Constantes.cacheMapaProcesos, allEntries = true)
    })
    public RespuestaWs reiniciarValores() {
        logger.info("reiniciarValores");
        return new RespuestaWs(Boolean.TRUE, "OK", "Valores actualizados");
    }

    public List<MenuTipo> findAllMenuTipo(MenuTipo data) {
        return _menuTipoRepo.findAll(Example.of(data));
    }

    public Menu find(Menu menu) {
        return _menuRepo.findOne(Example.of(menu)).orElse(null);
    }

    public List<Menu> findAll(Menu menu) {
//        System.out.println("checkerRedis.isRedisAvailable() " + checkerRedis.isRedisAvailable());
//        if (checkerRedis.isRedisAvailable()) {
            return findAllCache(menu);
//        } else {
//            return findAllNoCache(menu);
//        }
    }


    @Cacheable(cacheNames = Constantes.cacheMenus)
    public List<Menu> findAllCache(Menu menu) {
        return _menuRepo.findAll(Example.of(menu));
    }

    public List<Menu> findAllNoCache(Menu menu) {
        return _menuRepo.findAll(Example.of(menu));
    }


    public Menu save(Menu menu) {
        logger.log(Level.INFO, "save" + menu.toString());
        if (menu.getIdMenuPadre() != null) {
            menu.setMenuPadre(_menuRepo.findById(menu.getIdMenuPadre().intValue()).orElse(null));
        }
        if (menu.getTipo() == null) {
            menu.setMenuPadre(_menuRepo.findById(menu.getIdMenuPadre().intValue()).orElse(null));
        }
        if (menu.getId() != null && !Utils.isNullOrEmpty(menu.getMenuRolCollection())) {
            for (MenuRol menuRol : menu.getMenuRolCollection()) {
                menuRol.setMenu(menu);
            }
        }
        if (menu.getUrl() != null) {
            if (menu.getUrl().trim().length() == 0) {
                menu.setUrl(null);
            }
        }
        Menu save = _menuRepo.save(menu);
        save.setMenuRolCollection(null);
        save.setMenuPadre(new Menu(save.getMenuPadre().getId()));
        save.setMenusHijos_byNumPosicion(null);

        return save;
    }

    public void delete(Menu menu) {
        deleteAll(findAll(new MenuRol(new Menu(menu.getId()))));
        _menuRepo.delete(menu);
    }

    public List<Menu> findAll(Menu menu, Pageable pageable) {
        if (menu.getIdMenuPadre() != null) {
            if (menu.getIdMenuPadre() > 0) {
                menu.setMenuPadre(new Menu(menu.getIdMenuPadre().intValue()));
                menu.setIdMenuPadre(null);
            } else {
                return loadMenu(_menuRepo.findAllByMenuPadreIsNull());
            }
        }
        return loadMenu(_menuRepo.findAll(Example.of(menu), pageable).getContent());
    }

    public MenuTipo find(MenuTipo menuBar) {
        MenuTipo menuTipoDB = _menuTipoRepo.findOne(Example.of(menuBar)).orElse(null);
        if (menuTipoDB != null) {
            menuTipoDB.setMenuListSoyMenubar_byOrden(loadMenu(_menuRepo.findAll(Example.of(new Menu(menuTipoDB)))));
        }
        return menuTipoDB;
    }

    public List<MenuTipo> findAll(String user) {
//        if (checkerRedis.isRedisAvailable()) {
            return findAllCache(user);
//        } else {
//            return findAllNoCache(user);
//        }
    }

    public List<MenuTipo> findAllNoCache(String user) {
        try {
            List<Menu> list = new ArrayList<>();
            List<MenuTipo> menuUser = new ArrayList<>();
            Boolean usuarioEsAdministrador = _usuarioRolService.usuarioXrol(user, Constantes.rolAdministrador);
            if (!usuarioEsAdministrador) {
                List<Long> rolesUsuario = _usuarioRolService.getRolesUsuario(user);
                if (!Utils.isNullOrEmpty(rolesUsuario)) {
                    list = _menuRepo.findMenusPadresPublic();
                    if (Utils.isNullOrEmpty(list)) {
                        list = new ArrayList<>();
                    }
                    List<Menu> temp = _menuRepo.findByMenusPadresXroles(rolesUsuario);
                    if (!Utils.isNullOrEmpty(temp)) {
                        for (Menu me : temp) {
                            me.setMenusHijos_byNumPosicion(new ArrayList<>());
                            if (!list.contains(me)) {
                                list.add(me);
                            }
                        }
                    }
                    // HIjos
                    MenuTipo menuTipo = new MenuTipo(1, "Inicio");
                    if (!Utils.isNullOrEmpty(list)) {
                        menuTipo.setMenuListSoyMenubar_byOrden(this.loadMenu(list, rolesUsuario));
                        menuUser.add(menuTipo);
                    }
                }
            } else {
                BusquedaDinamica b = new BusquedaDinamica();
                b.setEntity("Menu");
                if (b.getFilters() == null) {
                    b.setFilters(new LinkedHashMap<>());
                }
                b.getFilters().put("menuPadre", new BusquedaDinamica.WhereCondition("ISNULL", null));
                b.getFilters().put("url", new BusquedaDinamica.WhereCondition("OR", Arrays.asList(new BusquedaDinamica.WhereCondition("ISNULL", null),
                        new BusquedaDinamica.WhereCondition("EQ", Arrays.asList("")))));
                if (b.getOrders() == null) {
                    b.setOrders(new LinkedHashMap<>());
                }
                b.getOrders().put("numPosicion", "ASC");
                List<Menu> menus = dynamicRepo.findAllDinamic("Menu", b);
                if (!Utils.isNullOrEmpty(menus)) {
                    list.addAll(menus);
                }
                MenuTipo menuTipo = new MenuTipo(1, "Inicio");
                if (!Utils.isNullOrEmpty(list)) {
                    //System.out.println("Menus encontrados ---> " + list.size());
                    menuTipo.setMenuListSoyMenubar_byOrden(this.loadMenu(list));
                    menuUser.add(menuTipo);
                }
            }
            return menuUser;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Cacheable(cacheNames = Constantes.cacheMenusXusuario)
    public List<MenuTipo> findAllCache(String user) {
        try {
            List<Menu> list = new ArrayList<>();
            List<MenuTipo> menuUser = new ArrayList<>();
            Boolean usuarioEsAdministrador = _usuarioRolService.usuarioXrol(user, Constantes.rolAdministrador);
            if (!usuarioEsAdministrador) {
                List<Long> rolesUsuario = _usuarioRolService.getRolesUsuario(user);
                if (!Utils.isNullOrEmpty(rolesUsuario)) {
                    list = _menuRepo.findMenusPadresPublic();
                    if (Utils.isNullOrEmpty(list)) {
                        list = new ArrayList<>();
                    }
                    List<Menu> temp = _menuRepo.findByMenusPadresXroles(rolesUsuario);
                    if (!Utils.isNullOrEmpty(temp)) {
                        for (Menu me : temp) {
                            me.setMenusHijos_byNumPosicion(new ArrayList<>());
                            if (!list.contains(me)) {
                                list.add(me);
                            }
                        }
                    }
                    // HIjos
                    MenuTipo menuTipo = new MenuTipo(1, "Inicio");
                    if (!Utils.isNullOrEmpty(list)) {
                        menuTipo.setMenuListSoyMenubar_byOrden(this.loadMenu(list, rolesUsuario));
                        menuUser.add(menuTipo);
                    }
                }
            } else {
                BusquedaDinamica b = new BusquedaDinamica();
                b.setEntity("Menu");
                if (b.getFilters() == null) {
                    b.setFilters(new LinkedHashMap<>());
                }
                b.getFilters().put("menuPadre", new BusquedaDinamica.WhereCondition("ISNULL", null));
                b.getFilters().put("url", new BusquedaDinamica.WhereCondition("OR", Arrays.asList(new BusquedaDinamica.WhereCondition("ISNULL", null),
                        new BusquedaDinamica.WhereCondition("EQ", Arrays.asList("")))));
                if (b.getOrders() == null) {
                    b.setOrders(new LinkedHashMap<>());
                }
                b.getOrders().put("numPosicion", "ASC");
                List<Menu> menus = dynamicRepo.findAllDinamic("Menu", b);
                if (!Utils.isNullOrEmpty(menus)) {
                    list.addAll(menus);
                }
                MenuTipo menuTipo = new MenuTipo(1, "Inicio");
                if (!Utils.isNullOrEmpty(list)) {
                    //System.out.println("Menus encontrados ---> " + list.size());
                    menuTipo.setMenuListSoyMenubar_byOrden(this.loadMenu(list));
                    menuUser.add(menuTipo);
                }
            }
            return menuUser;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<MenuTipo> findAll(MenuTipo menuBar) {
        List<MenuTipo> menus = _menuTipoRepo.findAll(Example.of(menuBar));
        for (MenuTipo menuTipo : menus) {
            menuTipo.setMenuListSoyMenubar_byOrden(loadMenu(_menuRepo.findAll(Example.of(new Menu(menuTipo)), Sort.by(Sort.Direction.ASC, "numPosicion"))));
        }
        return menus;
    }

    public MenuRol find(MenuRol menuRol) {
        return _menuRolRepo.findOne(Example.of(menuRol)).orElse(null);
    }

    public List<MenuRol> findAll(MenuRol menuRol) {
        return _menuRolRepo.findAll(Example.of(menuRol));
    }


    public MenuRol save(MenuRol menuRol) {
        logger.log(Level.INFO, "save" + menuRol.toString());
        return _menuRolRepo.save(menuRol);
    }

    //    @Transactional
    public Boolean delete(MenuRol menuRol) {
        try {
            Optional<MenuRol> menuRolOptional = _menuRolRepo.findById(menuRol.getId());
            if (menuRolOptional.isPresent()) {
//                System.out.println("delete:" + menuRolOptional.get());
                try {
                    _menuRolRepo.deleteXid(menuRolOptional.get().getId());
                } catch (Exception s) {
                    System.out.println("Eliminar menu error con exito...");
                }
//                _menuRolRepo.delete(menuRolOptional.get());
            } else {
                return Boolean.FALSE;
            }
            return Boolean.TRUE;
        } catch (Exception e) {
            logger.log(Level.SEVERE, "", e);
        }
        return Boolean.FALSE;
    }

    public void deleteAll(List<MenuRol> menuRols) {
        _menuRolRepo.deleteAll(menuRols);
    }


    private List<Menu> loadMenu(List<Menu> menus, List<Long> roles) {
        for (Menu m : menus) {
            m.setMenusHijos_byNumPosicion(new ArrayList<>());
            List<Menu> menusHijos = _menuRepo.findAllByMenuPadreXroles(m.getId(), roles);
            List<Menu> publicos = _menuRepo.findMenuPublicByMenuPadre(m.getId());
            // menusHijos.addAll(publicos);
            for (Menu mp : publicos) {
                if (menusHijos.contains(mp)) {
                    int indexOf = menusHijos.indexOf(mp);
                    if (indexOf >= 0)
                        menusHijos.set(indexOf, mp);
                } else {
                    menusHijos.add(mp);
                }
            }
            // Eliminar repetidos
            if (org.sas.seguridad.util.Utils.isNotEmpty(menusHijos)) {
                m.setMenusHijos_byNumPosicion(this.loadMenu(menusHijos, roles));
            }
        }
        return menus;
    }

    private List<Menu> loadMenuOld(List<Menu> menus, List<Long> roles) {
        for (Menu menu : menus) {
            menu.setMenusHijos_byNumPosicion(new ArrayList<>());
            for (Long rol : roles) {
                List<Menu> menusHijos = _menuRepo.findAllByMenuPadreXrol(menu.getId(), rol);
                if (org.sas.seguridad.util.Utils.isNotEmpty(menusHijos)) {
                    menu.setMenusHijos_byNumPosicion(this.loadMenu(menusHijos, roles));
                }
            }
        }
        return menus;
    }

    private List<Menu> loadMenuOld(List<Menu> menus) {
        Menu menuPadre;
        for (Menu menu : menus) {//SI ALGUN DIA LEEN ESTO RECUERDER QUE SI SU MENU NO SALE ES PORQUE NO TIENE UN TIPO ASIGNADO EN LA BASE GRACIAS AMIGUITOS
            if (menu.getMenuPadre() == null) {//ES UN MENU PADRE DEL MENU BAR
                menuPadre = new Menu();
                menuPadre.setTipo(new MenuTipo(1));
                menuPadre.setMenuPadre(new Menu(menu.getId()));
                List<Menu> menuhijos = _menuRepo.findAll(Example.of(menuPadre), Sort.by(Sort.Direction.ASC, "numPosicion"));
                if (org.sas.seguridad.util.Utils.isNotEmpty(menuhijos)) {
                    menu.setMenusHijos_byNumPosicion(loadMenu(menuhijos));
                }
            } else {//TIENE MENU PADRE
                menu.setIdMenuPadre(menu.getMenuPadre().getId().longValue());
                menuPadre = new Menu();
                menuPadre.setTipo(new MenuTipo(1));
                menuPadre.setMenuPadre(new Menu(menu.getId()));
                List<Menu> menuhijos = _menuRepo.findAll(Example.of(menuPadre), Sort.by(Sort.Direction.ASC, "numPosicion"));
                if (org.sas.seguridad.util.Utils.isNotEmpty(menuhijos)) {
                    menu.setMenusHijos_byNumPosicion(loadMenu(menuhijos));
                }
            }
        }
        return menus;
    }

    private List<Menu> loadMenu(List<Menu> menus) {
        for (Menu menu : menus) {
            if (menu.getMenuPadre() != null) {
                menu.setIdMenuPadre(menu.getMenuPadre().getId().longValue());
            }
            List<Menu> menuhijos = _menuRepo.findAllMenusByMenuPadre(menu.getId());
            if (org.sas.seguridad.util.Utils.isNotEmpty(menuhijos)) {
                menu.setMenusHijos_byNumPosicion(loadMenu(menuhijos));
            }
        }
        return menus;
    }

}
