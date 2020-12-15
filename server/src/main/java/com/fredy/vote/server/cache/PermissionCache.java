package com.fredy.vote.server.cache;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author Fredy
 * @date 2020-12-15 16:29
 */
public class PermissionCache {

    public static Set<String> adminPermissions() {
        List<String> permissionList = Arrays.asList("user:list", "user:add", "user:update", "user:delete", "theme:list", "theme:add", "theme:update", "theme:delete");
        Set<String> permissionSet = new HashSet<>(permissionList);
        return permissionSet;
    }
}
