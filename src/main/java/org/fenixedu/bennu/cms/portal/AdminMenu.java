package org.fenixedu.bennu.cms.portal;

import org.fenixedu.bennu.cms.domain.Menu;
import org.fenixedu.bennu.cms.domain.Site;
import org.fenixedu.bennu.spring.portal.BennuSpringController;
import org.fenixedu.commons.i18n.I18N;
import org.fenixedu.commons.i18n.LocalizedString;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import pt.ist.fenixframework.Atomic;

import com.google.common.base.Strings;

@BennuSpringController(AdminSites.class)
@RequestMapping("/cms/menus")
public class AdminMenu {
    @RequestMapping(value="{slug}", method = RequestMethod.GET)
    public String posts(Model model, @PathVariable(value="slug") String slug){
        Site site = Site.fromSlug(slug);
        model.addAttribute("site", site);
        model.addAttribute("menus", site.getMenusSet());
        return "menus";
    }

    @RequestMapping(value="{slug}/create", method = RequestMethod.GET)
    public String createMenu(Model model, @PathVariable(value="slug") String slug){
        Site s = Site.fromSlug(slug);
        model.addAttribute("site", s);
        return "createMenu";
    }

    @RequestMapping(value="{slug}/create", method = RequestMethod.POST)
    public RedirectView createMenu(Model model, @PathVariable(value = "slug") String slug, @RequestParam String name,
            RedirectAttributes redirectAttributes) {
        if (Strings.isNullOrEmpty(name)) {
            redirectAttributes.addFlashAttribute("emptyName", true);
            return new RedirectView("/cms/menus/" + slug + "/create", true);
        } else {
            Site s = Site.fromSlug(slug);
            createMenu(s, name);
            return new RedirectView("/cms/menus/" + s.getSlug() + "", true);
        }
    }

    @Atomic
    private void createMenu(Site site, String name) {
        Menu p = new Menu();
        p.setSite(site);
        p.setName(new LocalizedString(I18N.getLocale(),name));
    }

    @RequestMapping(value = "{slugSite}/{oidMenu}/delete", method = RequestMethod.POST)
    public RedirectView delete(Model model, @PathVariable(value="slugSite") String slugSite, @PathVariable(value="oidMenu") String oidMenu){
        Site s = Site.fromSlug(slugSite);
        s.menuForOid(oidMenu).delete();
        return new RedirectView("/cms/menus/" + s.getSlug() + "",true);
    }
}
