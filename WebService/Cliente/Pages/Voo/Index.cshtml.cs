using System;
using System.Threading.Tasks;
using Microsoft.AspNetCore.Mvc;
using Microsoft.AspNetCore.Mvc.RazorPages;

namespace RazorPagesMovie.Pages
{
    public class VooModel : PageModel
    {
        public string Message { get; set; }

        public void OnGet()
        {
            Message = "Pesquise por voos";
        }
    }
}
