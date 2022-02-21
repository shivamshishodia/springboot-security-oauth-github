package com.shishodia.security.oAuthGithub.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/home/")
public class HomeController {

    @GetMapping(path = "username")
    public String username(@AuthenticationPrincipal(expression = "attributes['name']") String username) {
        return username;
    }

    /**
    * login=shivamshishodia
    * id=21097868
    * node_id=MDQ6VXNlcjIxMDk3ODY4
    * avatar_url=https://avatars.githubusercontent.com/u/21097868?v=4
    * gravatar_id=
    * url=https://api.github.com/users/shivamshishodia
    * html_url=https://github.com/shivamshishodia
    * followers_url=https://api.github.com/users/shivamshishodia/followers
    * following_url=https://api.github.com/users/shivamshishodia/following{/other_user}
    * gists_url=https://api.github.com/users/shivamshishodia/gists{/gist_id}
    * starred_url=https://api.github.com/users/shivamshishodia/starred{/owner}{/repo}
    * subscriptions_url=https://api.github.com/users/shivamshishodia/subscriptions
    * organizations_url=https://api.github.com/users/shivamshishodia/orgs
    * repos_url=https://api.github.com/users/shivamshishodia/repos
    * events_url=https://api.github.com/users/shivamshishodia/events{/privacy}
    * received_events_url=https://api.github.com/users/shivamshishodia/received_events
    * type=User
    * site_admin=false
    * name=Shivam Shishodia
    * company=@dell
    * blog=https://shishodia.com
    * location=Bengaluru
    * email=shivam@shishodia.com
    * hireable=null
    * bio=null
    * twitter_username=shivamshishodia
    * public_repos=9
    * public_gists=0
    * followers=0
    * following=2
    * created_at=2016-08-18T06:59:38Z
    * updated_at=2022-01-15T13:54:02Z
    * private_gists=0
    * total_private_repos=5
    * owned_private_repos=5
    * disk_usage=5039
    * collaborators=0
    * two_factor_authentication=false
    * plan={name=free, space=976562499, collaborators=0, private_repos=10000}
    */
    
}
