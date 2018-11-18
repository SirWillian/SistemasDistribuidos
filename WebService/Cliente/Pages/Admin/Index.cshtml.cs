using System;
using System.Threading.Tasks;
using Microsoft.AspNetCore.Mvc;
using Microsoft.AspNetCore.Mvc.RazorPages;

namespace RazorPagesMovie.Pages
{
    public class AdminModel : PageModel
    {
        public string Message { get; set; }

        public void OnGet()
        {
            Message = "Página de administração";
        }
    }
}
