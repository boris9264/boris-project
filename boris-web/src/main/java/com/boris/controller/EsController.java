package com.boris.controller;

import com.boris.common.utils.ElasticsearchUtil;
import com.boris.vo.es.IndexVo;
import com.boris.vo.es.ProductVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(description="elasticsearch demo controller")
@Controller
@RequestMapping("/es")
public class EsController {

    @ApiOperation(value="新增索引", notes="新增索引", httpMethod = "POST")
    @RequestMapping(value = "/index", method = RequestMethod.POST)
    @ResponseBody
    public boolean createIndex(@RequestBody IndexVo indexVo) {
        return ElasticsearchUtil.createIndex(indexVo.getIndexName());
    }

    @ApiOperation(value="删除索引", notes="删除索引", httpMethod = "DELETE")
    @RequestMapping(value = "/index/{indexName}", method = RequestMethod.DELETE)
    @ResponseBody
    public boolean deleteIndex(@ApiParam(name = "indexName", value = "索引名称")
                                   @PathVariable("indexName") String indexName) {
        return ElasticsearchUtil.deleteIndex(indexName);
    }

    @ApiOperation(value="向索引中添加数据或根据ID修改数据", notes="向索引中添加数据或根据ID修改数据", httpMethod = "PUT")
    @RequestMapping(value = "/doc", method = RequestMethod.PUT)
    @ResponseBody
    public boolean createOrUpdateDoc(@RequestBody ProductVo productVo) {
        return ElasticsearchUtil.createOrUpdateDoc(productVo);
    }

    @ApiOperation(value="从索引中获取数据", notes="从索引中获取数据", httpMethod = "POST")
    @RequestMapping(value = "/doc", method = RequestMethod.POST)
    @ResponseBody
    public ProductVo getDoc(@RequestBody ProductVo productVo) {
        return ElasticsearchUtil.getDoc(productVo);
    }

    @ApiOperation(value="从索引中删除数据", notes="从索引中删除数据", httpMethod = "POST")
    @RequestMapping(value = "/deleteDoc", method = RequestMethod.POST)
    @ResponseBody
    public boolean deleteDoc(@RequestBody ProductVo productVo) {
        return ElasticsearchUtil.deleteDoc(productVo);
    }

    @ApiOperation(value="从索引中批量新增数据", notes="从索引中批量新增数据", httpMethod = "POST")
    @RequestMapping(value = "/bulkCreate", method = RequestMethod.POST)
    @ResponseBody
    public boolean bulkCreate(@RequestBody List<ProductVo> productVos) {
        return ElasticsearchUtil.bulkCreate(productVos);
    }

    @ApiOperation(value="从索引中批量删除数据", notes="从索引中批量删除数据", httpMethod = "POST")
    @RequestMapping(value = "/bulkDelete", method = RequestMethod.POST)
    @ResponseBody
    public boolean bulkDelete(@RequestBody List<ProductVo> productVos) {
        return ElasticsearchUtil.bulkDelete(productVos);
    }

    @ApiOperation(value="获取全部数据", notes="获取全部数据", httpMethod = "GET")
    @RequestMapping(value = "/queryAll", method = RequestMethod.GET)
    @ResponseBody
    public void queryAll() {
        ElasticsearchUtil.queryAll();
    }
}
