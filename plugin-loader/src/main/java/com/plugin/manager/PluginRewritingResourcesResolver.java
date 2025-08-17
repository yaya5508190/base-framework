package com.plugin.manager;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.io.Resource;
import org.springframework.lang.Nullable;
import org.springframework.web.servlet.resource.ResourceResolver;
import org.springframework.web.servlet.resource.ResourceResolverChain;

import java.util.List;

public class PluginRewritingResourcesResolver implements ResourceResolver {
    private String pluginId;

    public PluginRewritingResourcesResolver(String pluginId) {
        this.pluginId = pluginId;
    }

    @Override
    public Resource resolveResource(HttpServletRequest request, @Nullable String requestPath, @Nullable List<? extends Resource> locations, ResourceResolverChain chain) {
        String newPath = rewrite(requestPath);
        return chain.resolveResource(request, newPath, locations);
    }

    @Override
    public String resolveUrlPath(@Nullable String resourcePath, @Nullable List<? extends Resource> locations, ResourceResolverChain chain) {
        String newPath = rewrite(resourcePath);
        return chain.resolveUrlPath(newPath, locations);
    }

    private String rewrite(String path) {
        String prefix = "/" + pluginId;
        if (path != null && path.startsWith(prefix)) {
            return path.replace(prefix, "");
        }
        return path;
    }
}
