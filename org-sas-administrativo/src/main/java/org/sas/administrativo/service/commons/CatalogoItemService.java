package org.sas.administrativo.service.commons;

import org.sas.administrativo.dto.RespuestaWs;
import org.sas.administrativo.dto.commons.CatalogoItemDto;
import org.sas.administrativo.entity.configuracion.Catalogo;
import org.sas.administrativo.entity.configuracion.CatalogoItem;
import org.sas.administrativo.mapper.commons.CatalogoItemMapper;
import org.sas.administrativo.repository.configuracion.CatalogoItemRepository;
import org.sas.administrativo.repository.configuracion.CatalogoRepo;
import org.sas.administrativo.util.Constantes;
import org.sas.administrativo.util.Utils;
import org.sas.administrativo.util.model.EstadoType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * The type Catalogo item service.
 */
@Service
public class CatalogoItemService {

    private static final Logger LOG = Logger.getLogger(CatalogoItemService.class.getName());

    @Autowired
    private CatalogoItemRepository repo;
    @Autowired
    private CatalogoRepo catalogoRepo;
    @Autowired
    private CatalogoItemMapper mapper;


    /**
     * Busca todos los registro encontrados con los filtros pasado en el parametro data, si el parametro el nulo busca
     * todos los datos que esten en la tabla.
     *
     * @param data     Con los campo que estan llenos realiza la busqueda.
     * @param pageable Modelo de spring que permite ordenar y el paginado de los datos.
     * @return Lista con los registro encontrados y el tamaño que se especifico con la posción de donde debe empezar a filtrarse de los registro.
     */
    public Page<CatalogoItem> findAllPage(CatalogoItem data, Pageable pageable) {
        try {
            if (data == null) {
                data = new CatalogoItem();
            }
            return repo.findAll(Example.of(data), pageable);
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "", e);
            return null;
        }
    }

    public List<CatalogoItemDto> listaItems(String codigo) {
        return repo.findByItemsCatalogo("ACTIVO", codigo, "ACTIVO").stream().map(x -> mapper.toDto(x)).collect(Collectors.toList());
    }

    public List<CatalogoItem> listaItemsEntidad(String codigo) {
        return repo.findByItemsCatalogo("ACTIVO", codigo, "ACTIVO");
    }

    public List<CatalogoItem> listaItems(Long idCatalogo) {
        List<CatalogoItem> result = repo.findAllByCatalogo_IdOrderByOrdenAsc(idCatalogo);
        if (Utils.isNotEmpty(result)) {
            return result;
        }
        return new ArrayList<>();
    }

    public CatalogoItemDto findById(Long id) {
        CatalogoItem result = repo.findById(id).get();
        if (result != null) {
            return mapper.toDto(result);
        }
        return null;
    }

    public CatalogoItemDto findByCodigoCatalogoAndcodigoItem(String codigoCatalogo, String codigoItem) {
        CatalogoItem result = repo.findByCatalogo_CodigoAndCodigo(codigoCatalogo, codigoItem);
        System.out.println("Resutl " + result);
        if (result != null) {
            return mapper.toDto(result);
        }
        return new CatalogoItemDto();
    }

    public CatalogoItem findByCodigoCatalogoCatalogotItem(String codigoCatalogo, String codigoItem) {
        return repo.findByCatalogo_CodigoAndCodigo(codigoCatalogo, codigoItem);

    }

    public List<CatalogoItem> findAllCatalogoItemBycodeCatalogo(String codigo) {
        try {
            return repo.findByCatalogo_CodigoOrderByOrden(codigo);
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "", e);
            return null;
        }
    }

    public CatalogoItem findByCodCatalogoAndCodItem(String cod_catalogo, String cod_item) {
        try {
            return repo.findByCatalogo_CodigoAndCodigo(cod_catalogo, cod_item);
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "", e);
            return null;
        }
    }

    public CatalogoItem findOne(CatalogoItem data) {
        return repo.findOne(Example.of(data)).orElse(null);
    }

    public List<CatalogoItem> findAllCatalogoItemBycodeCatalogoGroup(String codigo) {
        try {
            return repo.findByCatalogo_CodigoOrderByOrdenGroup(codigo);
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "", e);
            return null;
        }
    }

    public RespuestaWs save(CatalogoItem data) {
//        log.info("CatalogoItem {} " + data.toString());

        RespuestaWs resp = new RespuestaWs();
        if (data.getOrden() == null) {
            Integer secuen = repo.maxOrdenCatalogo(data.getCatalogo().getId());
            data.setOrden((secuen == null ? 0 : secuen) + 1);
        }
        data = this.repo.save(data);
        try {
            if (data != null) {
                resp.setEstado(true);
                resp.setMensaje("Datos procesados correctamente.");
                resp.setData(Utils.toJson(data));
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "", e);
            resp.setEstado(false);
            resp.setMensaje(e.getMessage());
        }
        return resp;
    }


    public CatalogoItem findCatalogoItemByCod(String codigo) {
        try {
            return repo.findByCodigoAndEstado(codigo, EstadoType.ACTIVO.name());
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "", e);
            return null;
        }
    }

    public List<CatalogoItem> findAll(CatalogoItem data) {
        return repo.findAll(Example.of(data), Sort.by(Sort.Direction.ASC, "texto"));
    }

    public List<CatalogoItem> findAllByCodigoAndEstado(String codigo) {
        return repo.findAllByCodigoAndEstado(codigo, "ACTIVO");
    }

    public RespuestaWs guardarCatalogo(Catalogo catalogo) {
        try {
//            log.info("catalogo " + catalogo.toString());
            catalogo = catalogoRepo.save(catalogo);
            return new RespuestaWs(Boolean.TRUE, null, Constantes.datosCorrecto);
        } catch (Exception ex) {
            return new RespuestaWs(Boolean.FALSE, null, Constantes.intenteNuevamente);
        }
    }

    public RespuestaWs guardarCatalogoItem(CatalogoItem catalogoItem) {
        try {
//            log.warn("catalogoItem {}", catalogoItem.toString());
            if (catalogoItem.getOrden() == null) {
                if (catalogoItem.getCatalogo() != null) {
                    Integer secuen = repo.maxOrdenCatalogo(catalogoItem.getCatalogo().getId());
                    catalogoItem.setOrden((secuen == null ? 0 : secuen) + 1);
                }
            }
            catalogoItem = repo.save(catalogoItem);
            return new RespuestaWs(Boolean.TRUE, null, Constantes.datosCorrecto);
        } catch (Exception ex) {
            return new RespuestaWs(Boolean.FALSE, null, Constantes.intenteNuevamente);
        }
    }




    public Integer secuencia(Catalogo catalogo) {
        Integer aux = repo.maxOrdenCatalogo(catalogo.getId());
        if (aux != null) {
            return aux + 1;
        } else {
            return 1;
        }
    }

    public CatalogoItemDto findCatalogoDto(String cod_catalogo, String cod_item) {
        try {
            return mapper.toDto(repo.findByCatalogo_CodigoAndCodigo(cod_catalogo, cod_item));
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "", e);
            return null;
        }
    }


}
